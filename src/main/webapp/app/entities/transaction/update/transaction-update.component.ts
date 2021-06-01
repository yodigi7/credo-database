import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;

  ticketsCollection: ITicket[] = [];
  membershipLevelsSharedCollection: IMembershipLevel[] = [];
  peopleSharedCollection: IPerson[] = [];
  eventsSharedCollection: IEvent[] = [];

  editForm = this.fb.group({
    id: [],
    totalAmount: [null, [Validators.min(0)]],
    date: [],
    genericSubItemsPurchased: [],
    costSubItemsPurchased: [null, [Validators.min(0)]],
    numberOfMemberships: [null, [Validators.min(0)]],
    donation: [null, [Validators.min(0)]],
    eventDonation: [null, [Validators.min(0)]],
    notes: [],
    tickets: [],
    membershipLevel: [],
    person: [],
    event: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected ticketService: TicketService,
    protected membershipLevelService: MembershipLevelService,
    protected personService: PersonService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackTicketById(index: number, item: ITicket): number {
    return item.id!;
  }

  trackMembershipLevelById(index: number, item: IMembershipLevel): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      totalAmount: transaction.totalAmount,
      date: transaction.date,
      genericSubItemsPurchased: transaction.genericSubItemsPurchased,
      costSubItemsPurchased: transaction.costSubItemsPurchased,
      numberOfMemberships: transaction.numberOfMemberships,
      donation: transaction.donation,
      eventDonation: transaction.eventDonation,
      notes: transaction.notes,
      tickets: transaction.tickets,
      membershipLevel: transaction.membershipLevel,
      person: transaction.person,
      event: transaction.event,
    });

    this.ticketsCollection = this.ticketService.addTicketToCollectionIfMissing(this.ticketsCollection, transaction.tickets);
    this.membershipLevelsSharedCollection = this.membershipLevelService.addMembershipLevelToCollectionIfMissing(
      this.membershipLevelsSharedCollection,
      transaction.membershipLevel
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, transaction.person);
    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, transaction.event);
  }

  protected loadRelationshipsOptions(): void {
    this.ticketService
      .query({ 'transactionId.specified': 'false' })
      .pipe(map((res: HttpResponse<ITicket[]>) => res.body ?? []))
      .pipe(map((tickets: ITicket[]) => this.ticketService.addTicketToCollectionIfMissing(tickets, this.editForm.get('tickets')!.value)))
      .subscribe((tickets: ITicket[]) => (this.ticketsCollection = tickets));

    this.membershipLevelService
      .query()
      .pipe(map((res: HttpResponse<IMembershipLevel[]>) => res.body ?? []))
      .pipe(
        map((membershipLevels: IMembershipLevel[]) =>
          this.membershipLevelService.addMembershipLevelToCollectionIfMissing(membershipLevels, this.editForm.get('membershipLevel')!.value)
        )
      )
      .subscribe((membershipLevels: IMembershipLevel[]) => (this.membershipLevelsSharedCollection = membershipLevels));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      totalAmount: this.editForm.get(['totalAmount'])!.value,
      date: this.editForm.get(['date'])!.value,
      genericSubItemsPurchased: this.editForm.get(['genericSubItemsPurchased'])!.value,
      costSubItemsPurchased: this.editForm.get(['costSubItemsPurchased'])!.value,
      numberOfMemberships: this.editForm.get(['numberOfMemberships'])!.value,
      donation: this.editForm.get(['donation'])!.value,
      eventDonation: this.editForm.get(['eventDonation'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      tickets: this.editForm.get(['tickets'])!.value,
      membershipLevel: this.editForm.get(['membershipLevel'])!.value,
      person: this.editForm.get(['person'])!.value,
      event: this.editForm.get(['event'])!.value,
    };
  }
}
