import { Component, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';
import { HouseAddress } from 'app/entities/house-address/house-address.model';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';
import { EditPersonSubformComponent } from 'app/custom-forms/edit-person-subform/edit-person-subform.component';
import { of } from 'rxjs';

@Component({
  selector: 'jhi-create-person',
  templateUrl: './create-person.component.html',
  styleUrls: ['./create-person.component.css'],
})
export class CreatePersonComponent {
  hoh = new Person();
  hasSpouse = false;
  @ViewChild(EditPersonSubformComponent, { static: true }) personSubform: EditPersonSubformComponent;

  rootPersonForm = this.fb.group({
    hoh: [],
    addresses: this.fb.array([]),
    notes: [],
    mailingLabel: [],
    receiveMail: [],
    spouse: [],
    temp: [],
  });

  constructor(private fb: FormBuilder, private personService: PersonService, private houseDetailsService: HouseDetailsService) {}

  resetPage(): void {
    this.hoh = new Person();
    this.hasSpouse = false;

    this.rootPersonForm = this.fb.group({
      hoh: [],
      addresses: this.fb.array([]),
      notes: [],
      mailingLabel: [],
      receiveMail: [],
      spouse: [],
    });
  }

  async submit(): Promise<void> {
    const person = new Person();
    person.id = 1;
    console.log(this.rootPersonForm);
    console.log(this.rootPersonForm.value);
    if (this.hoh.id) {
      this.updatePerson();
    } else {
      await this.createPerson();
    }
    window.scroll(0, 0);
  }

  updatePerson(): void {
    return;
  }

  async createPerson(): Promise<void> {
    // this.hoh = this.generatePersonFromForm(this.getFormGroup('hoh'), true);
    this.hoh = this.rootPersonForm.controls.hoh.value;
    await of(1).toPromise();
    // this.hoh.personsInHouses = [];

    // if (
    //   (<FormGroup[]>this.getFormArray('addresses').controls).filter(
    //     (addressForm: FormGroup) =>
    //       addressForm.get('address')?.value ||
    //       addressForm.get('city')?.value ||
    //       addressForm.get('state')?.value ||
    //       addressForm.get('zipcode')?.value
    //   ).length ||
    //   this.rootPersonForm.controls.receiveMail.value ||
    //   this.rootPersonForm.controls.mailingLabel.value
    // ) {
    //   const res = await this.houseDetailsService.create(this.hoh.houseDetails!).toPromise();
    //   this.hoh.houseDetails = res.body;
    //   this.hoh = this.hoh.houseDetails?.headOfHouse ?? this.hoh;
    // } else {
    // const res = await this.personService.create(this.hoh).toPromise();
    // this.hoh = res.body ?? this.hoh;
    // this.hoh.houseDetails = res.body ?? this.hoh.houseDetails;
    // }
    // this.resetPage();
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  generatePersonFromForm(personForm: FormGroup, isHoh: boolean): IPerson {
    const person = new Person();
    person.prefix = personForm.get('prefix')?.value;
    person.firstName = personForm.get('firstName')?.value;
    person.middleName = personForm.get('middleName')?.value;
    person.lastName = personForm.get('lastName')?.value;
    person.suffix = personForm.get('suffix')?.value;

    person.nameTag = personForm.get('nameTag')?.value;
    person.membershipLevel = personForm.get('membershipLevel')?.value;
    person.membershipStartDate = personForm.get('memberSince')?.value;
    person.membershipExpirationDate = personForm.get('memberExpDate')?.value;
    person.isDeceased = personForm.get('deceased')?.value;
    const notes = new PersonNotes();
    notes.notes = personForm.get('notes')?.value ?? null;
    person.notes = notes.notes ? notes : null;
    person.isHeadOfHouse = isHoh;
    person.phones = [];
    (<FormGroup[]>(<FormArray>personForm.get('phones')).controls)
      .filter((phoneForm: FormGroup) => phoneForm.get('number')?.value || phoneForm.get('type')?.value)
      .forEach((phoneForm: FormGroup) => {
        const phone = new PersonPhone();
        phone.person = this.deepCopy(person);
        phone.person.phones = null;
        phone.phoneNumber = phoneForm.get('number')?.value;
        phone.type = phoneForm.get('type')?.value;
        person.phones?.push(phone);
      });
    person.emails = [];
    (<FormGroup[]>(<FormArray>personForm.get('emails')).controls)
      .filter((emailForm: FormGroup) => emailForm.get('email')?.value || emailForm.get('type')?.value)
      .forEach((emailForm: FormGroup) => {
        const email = new PersonEmail();
        email.person = this.deepCopy(person);
        email.person.emails = null;
        email.email = emailForm.get('email')?.value;
        email.type = emailForm.get('type')?.value;
        person.emails?.push(email);
      });

    if (
      (this.getFormGroup('spouse').controls.deceased.value ||
        this.getFormGroup('spouse').controls.prefix.value ||
        this.getFormGroup('spouse').controls.firstName.value ||
        this.getFormGroup('spouse').controls.middleName.value ||
        this.getFormGroup('spouse').controls.lastName.value ||
        this.getFormGroup('spouse').controls.suffix.value ||
        this.getFormGroup('spouse').controls.nameTag.value ||
        this.getFormGroup('spouse').controls.membershipLevel.value ||
        this.getFormGroup('spouse').controls.memberSince.value ||
        this.getFormGroup('spouse').controls.memberExpDate.value) &&
      isHoh
    ) {
      console.log(this.getFormGroup('spouse'));
      let spouse = this.generatePersonFromForm(this.getFormGroup('spouse'), false);
      spouse = <IPerson>this.deepCopy(spouse);
      spouse.headOfHouse = null;
      person.spouse = spouse;
    }
    if (
      ((<FormGroup[]>this.getFormArray('addresses').controls).filter(
        (addressForm: FormGroup) =>
          addressForm.get('address')?.value ||
          addressForm.get('city')?.value ||
          addressForm.get('state')?.value ||
          addressForm.get('zipcode')?.value
      ).length ||
        this.rootPersonForm.get('receiveMail')?.value ||
        this.rootPersonForm.get('mailingLabel')?.value) &&
      isHoh
    ) {
      person.houseDetails = new HouseDetails();
      person.houseDetails.id = this.hoh.houseDetails?.id;
      person.houseDetails.addresses = [];
      (<FormGroup[]>this.getFormArray('addresses').controls)
        .filter(
          (addressForm: FormGroup) =>
            addressForm.get('address')?.value ||
            addressForm.get('city')?.value ||
            addressForm.get('state')?.value ||
            addressForm.get('zipcode')?.value
        )
        .forEach((addressForm: FormGroup) => {
          const address = new HouseAddress();
          // address.id = this.hoh.houseDetails?.addresses?.[0]?.id;
          address.streetAddress = addressForm.get('address')?.value;
          address.city = addressForm.get('city')?.value;
          address.state = addressForm.get('state')?.value;
          address.zipcode = addressForm.get('zipcode')?.value;
          for (const addressExisting of this.hoh.houseDetails?.addresses ?? []) {
            if (
              address.streetAddress === addressExisting.streetAddress &&
              address.city === addressExisting.city &&
              address.state === addressExisting.state &&
              address.zipcode === addressExisting.zipcode
            ) {
              address.id = addressExisting.id;
              address.type = addressExisting.type;
              address.mailEventNotificationSubscription = addressExisting.mailEventNotificationSubscription;
              address.mailNewsletterSubscription = addressExisting.mailNewsletterSubscription;
              break;
            }
          }
          person.houseDetails!.addresses!.push(address);
        });
      person.houseDetails.mailingLabel = personForm.get('mailingLabel')?.value;
      const baseHoh = <IPerson>this.deepCopy(person);
      baseHoh.houseDetails = null;
      person.houseDetails.headOfHouse = baseHoh;
    } else if (!isHoh) {
      person.headOfHouse = this.hoh;
    }
    return person;
  }

  /* eslint-disable */
  deepCopy(object: object): object {
    return JSON.parse(JSON.stringify(object));
  }
  /* eslint-enable */

  createAddressFormGroup(): FormGroup {
    return this.fb.group({
      address: [],
      city: [],
      state: [],
      zipcode: [],
    });
  }

  addAddressToForm(addressList: string): void {
    (<FormArray>this.rootPersonForm.get(addressList)).push(this.createAddressFormGroup());
  }

  createPersonFormGroup(): FormGroup {
    const blankPerson = new Person();
    blankPerson.membershipLevel = new MembershipLevel();
    const person = this.fb.group(blankPerson);
    return person;
  }

  addSpouse(): void {
    this.hasSpouse = true;
  }

  getFormArray(formArrayName: string): FormArray {
    return <FormArray>this.rootPersonForm.get(formArrayName);
  }

  getFormGroup(formGroupName: string): FormGroup {
    return <FormGroup>this.rootPersonForm.get(formGroupName);
  }

  getHohId(): any {
    // return (<number | null>this.rootPersonForm.controls.hoh.value?.id?.value) ?? null;
  }
}
