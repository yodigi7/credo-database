import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { HouseAddress } from 'app/entities/house-address/house-address.model';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';

@Component({
  selector: 'jhi-edit-person-subform',
  templateUrl: './edit-person-subform.component.html',
  styleUrls: ['./edit-person-subform.component.css'],
})
export class EditPersonSubformComponent {
  @Input() personFormGroup?: FormGroup;
  @Input() isHoh = false;
  membershipLevels: MembershipLevel[];

  constructor(private fb: FormBuilder, private membershipLevelService: MembershipLevelService) {
    this.membershipLevels = [];
    const initMembershipLevels = (res: EntityArrayResponseType): void => {
      this.membershipLevels = res.body ?? [];
    };
    membershipLevelService.query().subscribe(initMembershipLevels);
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
    // person.parish = personForm.get("parish")?.value;
    person.isHeadOfHouse = this.isHoh;
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
      (personForm.get('address')?.value ||
        personForm.get('city')?.value ||
        personForm.get('state')?.value ||
        personForm.get('zipcode')?.value ||
        personForm.get('receiveMail')?.value ||
        personForm.get('mailingLabel')?.value) &&
      isHoh
    ) {
      person.houseDetails = new HouseDetails();
      person.houseDetails.addresses = [new HouseAddress()];
      person.houseDetails.addresses[0].streetAddress = personForm.get('address')?.value;
      person.houseDetails.addresses[0].city = personForm.get('city')?.value;
      person.houseDetails.addresses[0].state = personForm.get('state')?.value;
      person.houseDetails.addresses[0].zipcode = personForm.get('zipcode')?.value;
      person.houseDetails.addresses[0].mailNewsletterSubscription = personForm.get('receiveMail')?.value ?? YesNoEmpty.EMPTY;
      person.houseDetails.addresses[0].mailEventNotificationSubscription = personForm.get('receiveMail')?.value ?? YesNoEmpty.EMPTY;
      person.houseDetails.mailingLabel = personForm.get('mailingLabel')?.value;
      const baseHoh = <IPerson>this.deepCopy(person);
      baseHoh.houseDetails = null;
      person.houseDetails.headOfHouse = baseHoh;
    }
    return person;
  }

  /* eslint-disable */
  deepCopy(object: object): object {
    return JSON.parse(JSON.stringify(object));
  }
  /* eslint-enable */

  createPhoneFormGroup(): FormGroup {
    return this.fb.group({
      number: [],
      type: [],
    });
  }

  addPhoneToForm(phoneList: string): void {
    (<FormArray>this.personFormGroup?.get(phoneList)).push(this.createPhoneFormGroup());
  }

  createEmailFormGroup(): FormGroup {
    return this.fb.group({
      email: [],
      type: [],
    });
  }

  addEmailToForm(emailList: string): void {
    (<FormArray>this.personFormGroup?.get(emailList)).push(this.createEmailFormGroup());
  }

  getFormArray(formArrayName: string): FormArray {
    return <FormArray>this.personFormGroup?.get(formArrayName);
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }
}
