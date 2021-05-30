import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPerson, Person } from 'app/entities/person/person.model';

@Component({
  selector: 'jhi-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css'],
})
export class AddTransactionComponent implements OnInit {
  addTransaction: FormGroup;
  person: IPerson;
  personForm: FormGroup;

  constructor(private fb: FormBuilder, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.addTransaction = this.fb.group({
      date: [],
      dollarAmount: [],
      numberOfTickets: [],
    });
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      this.loadFromPerson(person);
    });
  }

  resetForm(): void {
    this.addTransaction.reset();
    this.loadFromPerson(this.person);
  }

  loadFromPerson(person: IPerson): void {
    console.log(person);
    this.personForm = this.fb.group(person);
    this.personForm.disable();
  }

  submit(): void {
    console.log(this.addTransaction.controls);
  }
}
