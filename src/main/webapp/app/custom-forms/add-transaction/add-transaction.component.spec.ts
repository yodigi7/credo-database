import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventService } from 'app/entities/event/service/event.service';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { Person } from 'app/entities/person/person.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { Ticket } from 'app/entities/ticket/ticket.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { Transaction } from 'app/entities/transaction/transaction.model';
import * as dayjs from 'dayjs';
import { of } from 'rxjs';
import { AddTransactionComponent } from './add-transaction.component';

jest.mock('@angular/router');

describe('Component Tests', () => {
  describe('Add Transaction Component', () => {
    let comp: AddTransactionComponent;
    let fixture: ComponentFixture<AddTransactionComponent>;
    let activatedRoute: ActivatedRoute;
    let transactionService: TransactionService;
    let membershipLevelService: MembershipLevelService;
    let eventService: EventService;
    let ticketService: TicketService;
    const person = new Person(
      456,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      undefined,
      undefined,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule],
        declarations: [AddTransactionComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AddTransactionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddTransactionComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      transactionService = TestBed.inject(TransactionService);
      membershipLevelService = TestBed.inject(MembershipLevelService);
      eventService = TestBed.inject(EventService);
      ticketService = TestBed.inject(TicketService);

      comp = fixture.componentInstance;

      spyOn(membershipLevelService, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [
              { level: 'Gold', cost: 50 },
              { level: 'Rose', cost: 30 },
              { level: 'Green', cost: 15 },
            ],
          })
        )
      );
      spyOn(eventService, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ name: 'event name', date: dayjs() }],
          })
        )
      );
      spyOn(transactionService, 'create').and.returnValue(of(new Transaction()));
      spyOn(ticketService, 'create').and.returnValue(of(new Ticket()));
      activatedRoute.data = of({ person });
      comp.ngOnInit();
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        expect(comp.addTransaction).not.toBeFalsy();
        expect(comp.addTransaction.get('person')?.get('id')).not.toBeFalsy();
      });

      it('Correct fields should be disabled', () => {
        expect(comp.addTransaction.get('totalAmount')?.disabled).toBeTruthy();
        expect(comp.addTransaction.get('person')?.disabled).toBeTruthy();
      });

      it('Function calculateTotal should calculate correct amount', () => {
        comp.addTransaction.get('costPerTicket')?.setValue(1);
        comp.addTransaction.get('numberOfTickets')?.setValue(1);
        comp.addTransaction.get('eventDonationAmount')?.setValue(27);
        comp.addTransaction.get('itemDollarAmount')?.setValue(21);
        comp.addTransaction.get('membershipLevel')?.setValue({ cost: 100, level: 'Gold' });
        comp.addTransaction.get('numberOfMemberships')?.setValue(2.0);
        comp.addTransaction.get('donationAmount')?.setValue(1000123);
        comp.addTransaction.get('totalAmount')?.setValue(1);

        expect(comp.calculateTotalAmount()).toEqual(1000372);
      });

      it('Calls membership and event services to get data', () => {
        expect(membershipLevelService.query).toHaveBeenCalled();
        expect(eventService.query).toHaveBeenCalled();
      });

      it('Calls only transaction service if ticket information is absent', done => {
        comp.submit().then(() => {
          expect(ticketService.create).not.toHaveBeenCalled();
          expect(transactionService.create).toHaveBeenCalled();
          done();
        });
      });

      it('Calls ticket and transaction services to persist data when ticket info is present', done => {
        comp.addTransaction.get('costPerTicket')?.setValue(10);
        comp.addTransaction.get('numberOfTickets')?.setValue(1);
        comp.submit().then(() => {
          expect(ticketService.create).toHaveBeenCalledTimes(1);
          expect(transactionService.create).toHaveBeenCalledTimes(1);
          done();
        });
      });
    });
  });
});
