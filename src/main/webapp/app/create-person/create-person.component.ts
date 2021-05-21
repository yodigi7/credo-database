import { Component, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IPerson, Person } from 'app/entities/person/person.model';
import { HouseDetails } from 'app/entities/house-details/house-details.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';
import { IMembershipLevel, MembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { EditPersonSubformComponent } from 'app/custom-forms/edit-person-subform/edit-person-subform.component';

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
    this.rootPersonForm.reset();
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
    this.hoh = this.rootPersonForm.controls.hoh.value.value;
    console.log(this.hoh);
    this.hoh.notes = this.rootPersonForm.controls.notes.value;
    this.hoh.spouse = this.rootPersonForm.controls.spouse.value.value;
    console.log(this.hoh);
    this.hoh.isHeadOfHouse = true;
    this.hoh.personsInHouses = [];

    if (
      this.rootPersonForm.controls.addresses.value ||
      this.rootPersonForm.controls.receiveMail.value ||
      this.rootPersonForm.controls.mailingLabel.value
    ) {
      this.hoh.houseDetails = new HouseDetails();
      this.hoh.houseDetails.addresses = this.rootPersonForm.controls.addresses.value;
      console.log(this.hoh);
      const hohCopy = this.deepCopy(this.hoh) as IPerson;
      hohCopy.houseDetails = undefined;
      this.hoh.houseDetails.headOfHouse = hohCopy;
      console.log(this.hoh);
      const res = await this.houseDetailsService.create(this.hoh.houseDetails).toPromise();
      this.hoh.houseDetails = res.body;
      this.hoh = this.hoh.houseDetails?.headOfHouse ?? this.hoh;
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
}
