import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { EntityArrayResponseType, PersonService } from 'app/entities/person/service/person.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';
import { HouseAddress } from 'app/entities/house-address/house-address.model';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { PersonPhone } from 'app/entities/person-phone/person-phone.model';
import { PersonEmail } from 'app/entities/person-email/person-email.model';

@Component({
  selector: 'jhi-create-person',
  templateUrl: './create-person.component.html',
  styleUrls: ['./create-person.component.css'],
})
export class CreatePersonComponent {
  hoh: IPerson;
  membershipLevels: MembershipLevel[];

  createPersonForm = this.fb.group({
    prefix: [],
    firstName: [],
    middleName: [],
    lastName: [],
    suffix: [],
    address: [],
    city: [],
    state: [],
    zipcode: [],
    nameTag: [],
    membershipLevel: [],
    memberSince: [],
    memberExpDate: [],
    deceased: [false],
    notes: [],
    mailingLabel: [],
    parish: [],
    receiveMail: [],
    hohPhones: this.fb.array([]),
    hohEmails: this.fb.array([]),
  });

  constructor(
    private fb: FormBuilder,
    private personService: PersonService,
    private houseDetailsService: HouseDetailsService,
    private membershipLevelService: MembershipLevelService
  ) {
    this.hoh = new Person();

    this.membershipLevels = [];
    const initMembershipLevels = (res: EntityArrayResponseType): void => {
      this.membershipLevels = res.body ?? [];
    };
    membershipLevelService.query().subscribe(initMembershipLevels);
  }

  async submit(): Promise<void> {
    if (this.hoh.id) {
      this.updatePerson();
    } else {
      await this.createPerson();
    }
  }

  updatePerson(): void {
    return;
  }

  async createPerson(): Promise<void> {
    this.updateHohFromForm();
    if (
      this.createPersonForm.get('address')?.value ||
      this.createPersonForm.get('city')?.value ||
      this.createPersonForm.get('state')?.value ||
      this.createPersonForm.get('zipcode')?.value ||
      this.createPersonForm.get('receiveMail')?.value ||
      this.createPersonForm.get('mailingLabel')?.value
    ) {
      const res = await this.houseDetailsService.create(this.hoh.houseDetails!).toPromise();
      this.hoh.houseDetails = res.body;
      this.hoh = this.hoh.houseDetails?.headOfHouse ?? this.hoh;
    } else {
      const res = await this.personService.create(this.hoh).toPromise();
      this.hoh = res.body ?? this.hoh;
    }
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  updateHohFromForm(): void {
    this.hoh.prefix = this.createPersonForm.get('prefix')?.value;
    this.hoh.firstName = this.createPersonForm.get('firstName')?.value;
    this.hoh.middleName = this.createPersonForm.get('middleName')?.value;
    this.hoh.lastName = this.createPersonForm.get('lastName')?.value;
    this.hoh.suffix = this.createPersonForm.get('suffix')?.value;

    this.hoh.nameTag = this.createPersonForm.get('nameTag')?.value;
    this.hoh.membershipLevel = this.createPersonForm.get('membershipLevel')?.value;
    this.hoh.membershipStartDate = this.createPersonForm.get('memberSince')?.value;
    this.hoh.membershipExpirationDate = this.createPersonForm.get('memberExpDate')?.value;
    this.hoh.isDeceased = this.createPersonForm.get('deceased')?.value;
    const notes = new PersonNotes();
    notes.notes = this.createPersonForm.get('notes')?.value ?? null;
    this.hoh.notes = notes.notes ? notes : null;
    // this.hoh.parish = this.createPersonForm.get("parish")?.value;
    this.hoh.isHeadOfHouse = true;
    this.hoh.phones = [];
    (<FormGroup[]>(<FormArray>this.createPersonForm.get('hohPhones')).controls)
      .filter((phoneForm: FormGroup) => phoneForm.get('number')?.value || phoneForm.get('type')?.value)
      .forEach((phoneForm: FormGroup) => {
        const phone = new PersonPhone();
        phone.person = this.deepCopy(this.hoh);
        phone.person.phones = null;
        phone.phoneNumber = phoneForm.get('number')?.value;
        phone.type = phoneForm.get('type')?.value;
        this.hoh.phones?.push(phone);
      });
    this.hoh.emails = [];
    (<FormGroup[]>(<FormArray>this.createPersonForm.get('hohEmails')).controls)
      .filter((emailForm: FormGroup) => emailForm.get('email')?.value || emailForm.get('type')?.value)
      .forEach((emailForm: FormGroup) => {
        const email = new PersonEmail();
        email.person = this.deepCopy(this.hoh);
        email.person.emails = null;
        email.email = emailForm.get('email')?.value;
        email.type = emailForm.get('type')?.value;
        this.hoh.emails?.push(email);
      });

    if (
      this.createPersonForm.get('address')?.value ||
      this.createPersonForm.get('city')?.value ||
      this.createPersonForm.get('state')?.value ||
      this.createPersonForm.get('zipcode')?.value ||
      this.createPersonForm.get('receiveMail')?.value ||
      this.createPersonForm.get('mailingLabel')?.value
    ) {
      this.hoh.houseDetails = new HouseDetails();
      this.hoh.houseDetails.addresses = [new HouseAddress()];
      this.hoh.houseDetails.addresses[0].streetAddress = this.createPersonForm.get('address')?.value;
      this.hoh.houseDetails.addresses[0].city = this.createPersonForm.get('city')?.value;
      this.hoh.houseDetails.addresses[0].state = this.createPersonForm.get('state')?.value;
      this.hoh.houseDetails.addresses[0].zipcode = this.createPersonForm.get('zipcode')?.value;
      this.hoh.houseDetails.addresses[0].mailNewsletterSubscription = this.createPersonForm.get('receiveMail')?.value ?? YesNoEmpty.EMPTY;
      this.hoh.houseDetails.addresses[0].mailEventNotificationSubscription =
        this.createPersonForm.get('receiveMail')?.value ?? YesNoEmpty.EMPTY;
      this.hoh.houseDetails.mailingLabel = this.createPersonForm.get('mailingLabel')?.value;
      const baseHoh = <IPerson>this.deepCopy(this.hoh);
      baseHoh.houseDetails = null;
      this.hoh.houseDetails.headOfHouse = baseHoh;
    }
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
    (<FormArray>this.createPersonForm.get(phoneList)).push(this.createPhoneFormGroup());
  }

  createEmailFormGroup(): FormGroup {
    return this.fb.group({
      email: [],
      type: [],
    });
  }

  addEmailToForm(emailList: string): void {
    (<FormArray>this.createPersonForm.get(emailList)).push(this.createEmailFormGroup());
  }

  getFormArray(formArrayName: string): FormArray {
    return <FormArray>this.createPersonForm.get(formArrayName);
  }
}
