import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { ActivatedRoute } from '@angular/router';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { PersonNotesService } from 'app/entities/person-notes/service/person-notes.service';

@Component({
  selector: 'jhi-edit-person',
  templateUrl: './edit-person.component.html',
  styleUrls: ['./edit-person.component.css'],
})
export class EditPersonComponent implements OnInit {
  hoh = new Person();
  hasSpouse = false;

  rootPersonForm = this.fb.group({
    hoh: [],
    addresses: this.fb.array([]),
    notes: [],
    mailingLabel: [],
    receiveMail: [],
    spouse: [],
  });

  constructor(
    private fb: FormBuilder,
    private personService: PersonService,
    private houseDetailsService: HouseDetailsService,
    private activatedRoute: ActivatedRoute,
    private personNotesService: PersonNotesService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person) {
        this.loadFromPerson(person);
      }
    });
  }

  resetPage(): void {
    this.hoh = new Person();
    // const res = await this.personService.find(1).toPromise();
    // this.hoh = res.body ?? new Person();
    // this.rootPersonForm.controls.hoh.setValue(this.hoh);
    this.hasSpouse = false;
    this.rootPersonForm.reset();
  }

  loadFromPerson(person: IPerson): void {
    this.hoh = person;
    this.hasSpouse = this.hoh.spouse ? true : false;
    this.rootPersonForm.controls.hoh.setValue(this.hoh);
    // TODO: get from database
    this.rootPersonForm.controls.addresses.setValue(this.hoh.houseDetails?.addresses ?? []);
    this.rootPersonForm.controls.notes.setValue(this.hoh.notes?.notes);
    this.rootPersonForm.controls.spouse.setValue(this.hoh.spouse);
    console.log(this.hoh);
    console.log(this.rootPersonForm);
  }

  async submit(): Promise<void> {
    console.log(this.rootPersonForm.controls);
    console.log(this.hoh);
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
    this.hoh = this.rootPersonForm.controls.hoh.value.value;
    console.log(this.rootPersonForm.controls);
    console.log(this.hoh);
    const notes = new PersonNotes();
    notes.notes = this.rootPersonForm.controls.notes.value;
    this.hoh.notes = notes;
    this.hoh.spouse = this.hasSpouse ? this.rootPersonForm.controls.spouse.value.value : null;
    this.hoh.isHeadOfHouse = true;
    this.hoh.personsInHouses = [];
    console.log(this.hoh);

    if (
      this.rootPersonForm.controls.addresses.value ||
      this.rootPersonForm.controls.receiveMail.value ||
      this.rootPersonForm.controls.mailingLabel.value
    ) {
      this.hoh.houseDetails = new HouseDetails();
      this.hoh.houseDetails.addresses = this.rootPersonForm.controls.addresses.value;
      const hohCopy = this.deepCopy(this.hoh) as IPerson;
      this.hoh.houseDetails.headOfHouse = { ...this.hoh };
      this.hoh.houseDetails.headOfHouse.houseDetails = null;
      console.log({ ...this.hoh });
      const res = await this.houseDetailsService.create(this.hoh.houseDetails).toPromise();
      this.hoh.houseDetails = res.body;
      this.hoh = this.hoh.houseDetails?.headOfHouse ?? this.hoh;
      // notes.person = this.hoh;
      // await this.personNotesService.create(notes).toPromise();
    } else {
      const res = await this.personService.create(this.hoh).toPromise();
      this.hoh = res.body ?? this.hoh;
      this.hoh.houseDetails = res.body ?? this.hoh.houseDetails;
    }
    this.resetPage();
    console.log(this.rootPersonForm);
    console.log(this.hoh);
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  /* eslint-disable */
  deepCopy(object: object): any {
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
}
