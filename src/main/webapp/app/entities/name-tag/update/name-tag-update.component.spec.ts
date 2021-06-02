jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NameTagService } from '../service/name-tag.service';
import { INameTag, NameTag } from '../name-tag.model';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';

import { NameTagUpdateComponent } from './name-tag-update.component';

describe('Component Tests', () => {
  describe('NameTag Management Update Component', () => {
    let comp: NameTagUpdateComponent;
    let fixture: ComponentFixture<NameTagUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nameTagService: NameTagService;
    let ticketService: TicketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NameTagUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NameTagUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NameTagUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nameTagService = TestBed.inject(NameTagService);
      ticketService = TestBed.inject(TicketService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Ticket query and add missing value', () => {
        const nameTag: INameTag = { id: 456 };
        const ticket: ITicket = { id: 74307 };
        nameTag.ticket = ticket;

        const ticketCollection: ITicket[] = [{ id: 63574 }];
        spyOn(ticketService, 'query').and.returnValue(of(new HttpResponse({ body: ticketCollection })));
        const additionalTickets = [ticket];
        const expectedCollection: ITicket[] = [...additionalTickets, ...ticketCollection];
        spyOn(ticketService, 'addTicketToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ nameTag });
        comp.ngOnInit();

        expect(ticketService.query).toHaveBeenCalled();
        expect(ticketService.addTicketToCollectionIfMissing).toHaveBeenCalledWith(ticketCollection, ...additionalTickets);
        expect(comp.ticketsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const nameTag: INameTag = { id: 456 };
        const ticket: ITicket = { id: 45076 };
        nameTag.ticket = ticket;

        activatedRoute.data = of({ nameTag });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nameTag));
        expect(comp.ticketsSharedCollection).toContain(ticket);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nameTag = { id: 123 };
        spyOn(nameTagService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nameTag });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nameTag }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nameTagService.update).toHaveBeenCalledWith(nameTag);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nameTag = new NameTag();
        spyOn(nameTagService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nameTag });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nameTag }));
        saveSubject.complete();

        // THEN
        expect(nameTagService.create).toHaveBeenCalledWith(nameTag);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nameTag = { id: 123 };
        spyOn(nameTagService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nameTag });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nameTagService.update).toHaveBeenCalledWith(nameTag);
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
    });
  });
});
