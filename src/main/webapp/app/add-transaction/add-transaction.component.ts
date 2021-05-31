import { Location } from '@angular/common';
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

  constructor(private fb: FormBuilder, private activatedRoute: ActivatedRoute, private location: Location) {}

  ngOnInit(): void {
    this.addTransaction = this.fb.group({
      date: [],
      costPerTicket: [],
      numberOfTickets: [],
      ticketEvent: [],
      eventDonationAmount: [],
      donationEvent: [],
      itemDescriptions: [],
      itemDollarAmount: [],
      membershipLevel: [],
      numberOfMemberships: [],
      donationAmount: [],
      person: this.fb.group(new Person()),
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
    this.addTransaction.get('person')?.setValue(person);
    // this.addTransaction.get('person')?.disable();
  }

  submit(): void {
    this.location.back();
  }
}
