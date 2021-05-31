import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DATE_FORMAT } from 'app/config/input.constants';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IPerson, Person } from 'app/entities/person/person.model';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { Transaction } from 'app/entities/transaction/transaction.model';
import * as dayjs from 'dayjs';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css'],
})
export class AddTransactionComponent implements OnInit, OnDestroy {
  addTransaction: FormGroup;
  person: IPerson;
  membershipList: IMembershipLevel[];
  subscribers: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private location: Location,
    private transactionService: TransactionService,
    private membershipLevelService: MembershipLevelService
  ) {}

  ngOnInit(): void {
    this.addTransaction = this.fb.group({
      date: [dayjs(new Date()).format(DATE_FORMAT), [Validators.required]],
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
      totalAmount: [],
      person: this.fb.group(new Person()),
    });
    this.addTransaction.get('totalAmount')?.disable();
    this.subscribers.push(
      this.activatedRoute.data.subscribe(({ person }) => {
        this.person = person;
        this.loadFromPerson(person);
      })
    );
    this.subscribers.push(
      this.membershipLevelService.query().subscribe((res: EntityArrayResponseType) => {
        this.membershipList = res.body ?? [];
      })
    );
  }

  resetForm(): void {
    this.addTransaction.reset();
    this.loadFromPerson(this.person);
  }

  loadFromPerson(person: IPerson): void {
    this.addTransaction.get('person')?.setValue(person);
    this.addTransaction.get('person')?.disable();
  }

  submit(): void {
    const transaction = new Transaction();
    transaction.date = dayjs(this.addTransaction.get('date')?.value, DATE_FORMAT);
    transaction.membershipLevel = this.addTransaction.get('membershipLevel')?.value;
    transaction.numberOfMemberships = this.addTransaction.get('numberOfMemberships')?.value;
    transaction.genericSubItemsPurchased = this.addTransaction.get('itemDescriptions')?.value;
    transaction.costSubItemsPurchased = this.addTransaction.get('itemDollarAmount')?.value;
    transaction.totalAmount = this.calculateTotalAmount();
    transaction.person = this.person;
    transaction.donation = this.addTransaction.get('donationAmount')?.value;
    // public tickets?: ITicket | null,
    // public eventDonations?: IEventDonation | null,
    this.transactionService.create(transaction).subscribe(console.log);
    this.location.back();
  }

  calculateTotalAmount(): number {
    console.log(this.addTransaction.controls);
    return (
      (this.addTransaction.get('costPerTicket')?.value as number) * this.addTransaction.get('numberOfTickets')?.value +
      (this.addTransaction.get('eventDonationAmount')?.value as number) +
      (this.addTransaction.get('itemDollarAmount')?.value as number) +
      (this.addTransaction.get('donationAmount')?.value as number)
    );
  }

  ngOnDestroy(): void {
    this.subscribers.forEach(sub => sub.unsubscribe());
  }
}
