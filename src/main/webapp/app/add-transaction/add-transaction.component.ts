import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { tick } from '@angular/core/testing';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IPerson, Person } from 'app/entities/person/person.model';
import { EntityArrayResponseType } from 'app/entities/person/service/person.service';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { Ticket } from 'app/entities/ticket/ticket.model';
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
  eventList: IEvent[];
  subscribers: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private location: Location,
    private transactionService: TransactionService,
    private membershipLevelService: MembershipLevelService,
    private eventService: EventService,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    this.addTransaction = this.fb.group({
      date: [dayjs(new Date()).format(DATE_FORMAT), [Validators.required]],
      costPerTicket: [],
      numberOfTickets: [],
      eventDonationAmount: [],
      event: [],
      itemDescriptions: [],
      itemDollarAmount: [],
      membershipLevel: [],
      numberOfMemberships: [],
      donationAmount: [],
      totalAmount: [],
      person: this.fb.group(new Person()),
    });
    this.addTransaction.get('totalAmount')?.disable();
    this.addTransaction.get('person')?.disable();
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
    this.subscribers.push(
      this.eventService.query({ size: 100 }).subscribe((res: EntityArrayResponseType) => {
        this.eventList = res.body ?? [];
      })
    );
  }

  resetForm(): void {
    this.addTransaction.reset();
    this.loadFromPerson(this.person);
  }

  loadFromPerson(person: IPerson): void {
    this.addTransaction.get('person')?.setValue(person);
  }

  async submit(): Promise<void> {
    const transaction = new Transaction();
    transaction.date = dayjs(this.addTransaction.get('date')?.value, DATE_FORMAT);
    transaction.membershipLevel = this.addTransaction.get('membershipLevel')?.value;
    transaction.numberOfMemberships = this.addTransaction.get('numberOfMemberships')?.value;
    transaction.genericSubItemsPurchased = this.addTransaction.get('itemDescriptions')?.value;
    transaction.costSubItemsPurchased = this.addTransaction.get('itemDollarAmount')?.value;
    transaction.totalAmount = this.calculateTotalAmount();
    transaction.person = this.person;
    transaction.donation = this.addTransaction.get('donationAmount')?.value;
    transaction.eventDonation = this.addTransaction.get('eventDonationAmount')?.value;
    // TODO add event
    if (this.anyValue(['costPerTicket', 'numberOfTickets'])) {
      let ticket = new Ticket();
      ticket.costPerTicket = this.addTransaction.get('costPerTicket')?.value;
      ticket.count = this.addTransaction.get('numberOfTickets')?.value;
      ticket.person = this.person;
      ticket.event = this.addTransaction.get('event')?.value;
      const res = await this.ticketService.create(ticket).toPromise();
      ticket = res.body!;
      transaction.tickets = ticket;
    }
    await this.transactionService.create(transaction).toPromise();
    this.location.back();
  }

  anyValue(keys: string[]): boolean {
    return keys.filter(key => this.addTransaction.get(key)?.value).length > 0;
  }

  calculateTotalAmount(): number {
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
