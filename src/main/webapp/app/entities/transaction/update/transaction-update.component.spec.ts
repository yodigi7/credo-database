jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TransactionService } from '../service/transaction.service';
import { ITransaction, Transaction } from '../transaction.model';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { TransactionUpdateComponent } from './transaction-update.component';

describe('Component Tests', () => {
  describe('Transaction Management Update Component', () => {
    let comp: TransactionUpdateComponent;
    let fixture: ComponentFixture<TransactionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let transactionService: TransactionService;
    let ticketService: TicketService;
    let membershipLevelService: MembershipLevelService;
    let personService: PersonService;
    let eventService: EventService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TransactionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TransactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TransactionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      transactionService = TestBed.inject(TransactionService);
      ticketService = TestBed.inject(TicketService);
      membershipLevelService = TestBed.inject(MembershipLevelService);
      personService = TestBed.inject(PersonService);
      eventService = TestBed.inject(EventService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call tickets query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const tickets: ITicket = { id: 58122 };
        transaction.tickets = tickets;

        const ticketsCollection: ITicket[] = [{ id: 33758 }];
        spyOn(ticketService, 'query').and.returnValue(of(new HttpResponse({ body: ticketsCollection })));
        const expectedCollection: ITicket[] = [tickets, ...ticketsCollection];
        spyOn(ticketService, 'addTicketToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(ticketService.query).toHaveBeenCalled();
        expect(ticketService.addTicketToCollectionIfMissing).toHaveBeenCalledWith(ticketsCollection, tickets);
        expect(comp.ticketsCollection).toEqual(expectedCollection);
      });

      it('Should call MembershipLevel query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const membershipLevel: IMembershipLevel = { id: 88767 };
        transaction.membershipLevel = membershipLevel;

        const membershipLevelCollection: IMembershipLevel[] = [{ id: 75090 }];
        spyOn(membershipLevelService, 'query').and.returnValue(of(new HttpResponse({ body: membershipLevelCollection })));
        const additionalMembershipLevels = [membershipLevel];
        const expectedCollection: IMembershipLevel[] = [...additionalMembershipLevels, ...membershipLevelCollection];
        spyOn(membershipLevelService, 'addMembershipLevelToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(membershipLevelService.query).toHaveBeenCalled();
        expect(membershipLevelService.addMembershipLevelToCollectionIfMissing).toHaveBeenCalledWith(
          membershipLevelCollection,
          ...additionalMembershipLevels
        );
        expect(comp.membershipLevelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const person: IPerson = { id: 20012 };
        transaction.person = person;

        const personCollection: IPerson[] = [{ id: 92077 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Event query and add missing value', () => {
        const transaction: ITransaction = { id: 456 };
        const event: IEvent = { id: 48018 };
        transaction.event = event;

        const eventCollection: IEvent[] = [{ id: 56574 }];
        spyOn(eventService, 'query').and.returnValue(of(new HttpResponse({ body: eventCollection })));
        const additionalEvents = [event];
        const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
        spyOn(eventService, 'addEventToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(eventService.query).toHaveBeenCalled();
        expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
        expect(comp.eventsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const transaction: ITransaction = { id: 456 };
        const tickets: ITicket = { id: 81732 };
        transaction.tickets = tickets;
        const membershipLevel: IMembershipLevel = { id: 9270 };
        transaction.membershipLevel = membershipLevel;
        const person: IPerson = { id: 87853 };
        transaction.person = person;
        const event: IEvent = { id: 64253 };
        transaction.event = event;

        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(transaction));
        expect(comp.ticketsCollection).toContain(tickets);
        expect(comp.membershipLevelsSharedCollection).toContain(membershipLevel);
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.eventsSharedCollection).toContain(event);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const transaction = { id: 123 };
        spyOn(transactionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: transaction }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(transactionService.update).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const transaction = new Transaction();
        spyOn(transactionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: transaction }));
        saveSubject.complete();

        // THEN
        expect(transactionService.create).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const transaction = { id: 123 };
        spyOn(transactionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ transaction });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(transactionService.update).toHaveBeenCalledWith(transaction);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTicketById', () => {
        it('Should return tracked Ticket primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTicketById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMembershipLevelById', () => {
        it('Should return tracked MembershipLevel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMembershipLevelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEventById', () => {
        it('Should return tracked Event primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
