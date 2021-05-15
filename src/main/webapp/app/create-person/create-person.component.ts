import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';
import { HouseAddress } from 'app/entities/house-address/house-address.model';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';

@Component({
  selector: 'jhi-create-person',
  templateUrl: './create-person.component.html',
  styleUrls: ['./create-person.component.css'],
})
export class CreatePersonComponent {
  hoh: IPerson;

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
    deceased: [],
    notes: [],
    mailingLabel: [],
    parish: [],
    receiveMail: [],
  });

  constructor(private fb: FormBuilder, private personService: PersonService, private houseDetailsService: HouseDetailsService) {
    this.hoh = new Person();
  }

  async submit(): Promise<void> {
    this.hoh.prefix = this.createPersonForm.get('prefix')?.value;
    this.hoh.firstName = this.createPersonForm.get('firstName')?.value;
    this.hoh.middleName = this.createPersonForm.get('middleName')?.value;
    this.hoh.lastName = this.createPersonForm.get('lastName')?.value;
    this.hoh.suffix = this.createPersonForm.get('suffix')?.value;

    this.hoh.nameTag = this.createPersonForm.get('nameTag')?.value;
    // this.hoh.membershipLevel = this.createPersonForm.get("membershipLevel")?.value;
    this.hoh.membershipStartDate = this.createPersonForm.get('memberSince')?.value;
    this.hoh.membershipExpirationDate = this.createPersonForm.get('memberExpDate')?.value;
    this.hoh.isDeceased = this.createPersonForm.get('deceased')?.value ?? this.hoh.isDeceased;
    const notes = new PersonNotes();
    notes.notes = this.createPersonForm.get('notes')?.value ?? null;
    this.hoh.notes = notes.notes ? notes : null;
    // this.hoh.parish = this.createPersonForm.get("parish")?.value;
    this.hoh.isHeadOfHouse = true;

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
      const baseHoh = JSON.parse(JSON.stringify(this.hoh));
      this.hoh.houseDetails.headOfHouse = baseHoh;
      this.hoh.houseDetails.headOfHouse!.houseDetails = null;
      const res = await this.houseDetailsService.create(this.hoh.houseDetails).toPromise();
      this.hoh.houseDetails = res.body;
      this.hoh = this.hoh.houseDetails?.headOfHouse ?? this.hoh;
    } else {
      const res = await this.personService.create(this.hoh).toPromise();
      this.hoh = res.body ?? this.hoh;
    }
  }
}
