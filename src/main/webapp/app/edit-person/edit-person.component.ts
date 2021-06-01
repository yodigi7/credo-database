import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonNotes } from 'app/entities/person-notes/person-notes.model';
import { PersonNotesService } from 'app/entities/person-notes/service/person-notes.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';

@Component({
  selector: 'jhi-edit-person',
  templateUrl: './edit-person.component.html',
  styleUrls: ['./edit-person.component.css'],
})
export class EditPersonComponent implements OnInit {
  hoh = new Person();
  hasSpouse = false;
  states = [
    'AL',
    'AK',
    'AZ',
    'AR',
    'CA',
    'CO',
    'CT',
    'DE',
    'FL',
    'GA',
    'HI',
    'ID',
    'IL',
    'IN',
    'IA',
    'KS',
    'KY',
    'LA',
    'ME',
    'MD',
    'MA',
    'MI',
    'MN',
    'MS',
    'MO',
    'MT',
    'NE',
    'NV',
    'NH',
    'NJ',
    'NM',
    'NY',
    'NC',
    'ND',
    'OH',
    'OK',
    'OR',
    'PW',
    'PA',
    'RI',
    'SC',
    'SD',
    'TN',
    'TX',
    'UT',
    'VT',
    'VA',
    'WA',
    'WV',
    'WI',
    'WY',
  ];

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
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private houseDetailsService: HouseDetailsService,
    private personNotesService: PersonNotesService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person) {
        this.loadFromPerson(person).then(console.log);
      }
    });
  }

  resetPage(): void {
    if (this.router.url !== '/edit-person') {
      this.router.navigate(['..'], { relativeTo: this.activatedRoute });
    } else {
      this.hoh = new Person();
      this.hasSpouse = false;
      this.rootPersonForm.reset();
    }
  }

  async loadFromPerson(person: IPerson): Promise<void> {
    this.hoh = person;
    this.hasSpouse = this.hoh.spouse ? true : false;
    this.rootPersonForm.controls.hoh.setValue(this.hoh);
    this.rootPersonForm.setControl('addresses', this.fb.array(this.hoh.houseDetails?.addresses?.map(addr => this.fb.group(addr)) ?? []));
    this.rootPersonForm.controls.mailingLabel.setValue(this.hoh.houseDetails?.mailingLabel);
    this.rootPersonForm.controls.notes.setValue(this.hoh.notes?.notes);
    if (this.hoh.spouse?.id) {
      const spouse = await this.personService.find(this.hoh.spouse.id).toPromise();
      this.rootPersonForm.controls.spouse.setValue(spouse.body);
    }
  }

  async submit(): Promise<void> {
    if (this.hoh.id) {
      await this.updatePerson();
    } else {
      await this.createPerson();
    }
    window.scroll(0, 0);
    this.resetPage();
  }

  async updatePerson(): Promise<void> {
    let personNotesFn: any;
    let houseDetailsFn: any;

    if (this.hoh.houseDetails?.id) {
      houseDetailsFn = this.houseDetailsService.update.bind(this.houseDetailsService);
    } else {
      houseDetailsFn = this.houseDetailsService.create.bind(this.houseDetailsService);
    }
    if (this.hoh.notes?.id) {
      personNotesFn = this.personNotesService.update.bind(this.personNotesService);
    } else {
      personNotesFn = this.personNotesService.create.bind(this.personNotesService);
    }
    await this.sendPerson(this.personService.update.bind(this.personService), houseDetailsFn, personNotesFn);
  }

  async createPerson(): Promise<void> {
    await this.sendPerson(
      this.personService.create.bind(this.personService),
      this.houseDetailsService.create.bind(this.houseDetailsService),
      this.personNotesService.create.bind(this.personNotesService)
    );
  }

  async sendPerson(personSvcFn: any, houseDetailsSvcFn: any, personNotesSvcFn: any): Promise<void> {
    this.hoh = this.rootPersonForm.controls.hoh.value.value;
    this.hoh.spouse = this.hasSpouse ? this.rootPersonForm.controls.spouse.value.value : null;
    this.hoh.isHeadOfHouse = true;
    this.hoh.personsInHouses = [];
    const res = await personSvcFn(this.hoh).toPromise();
    this.hoh = res.body ?? new Person();
    if (
      this.rootPersonForm.get('addresses')?.value.length ||
      this.rootPersonForm.get('receiveMail')?.value ||
      this.rootPersonForm.get('mailingLabel')?.value
    ) {
      const houseDetails = new HouseDetails();
      houseDetails.id = this.rootPersonForm.controls.hoh.value.get('houseDetails')?.value?.id;
      houseDetails.addresses = this.rootPersonForm.controls.addresses.value;
      houseDetails.headOfHouse = { ...this.hoh };
      houseDetails.mailingLabel = this.rootPersonForm.controls.mailingLabel.value;
      houseDetails.headOfHouse.houseDetails = null;
      await houseDetailsSvcFn(houseDetails).toPromise();
    }
    if (this.rootPersonForm.get('notes')?.value) {
      const notes = new PersonNotes();
      notes.id = this.rootPersonForm.controls.hoh.value.get('notes')?.value?.id;
      notes.notes = this.rootPersonForm.controls.notes.value;
      notes.person = { ...this.hoh };
      notes.person.houseDetails = undefined;
      await personNotesSvcFn(notes).toPromise();
    }
    this.resetPage();
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
      streetAddress: [],
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
