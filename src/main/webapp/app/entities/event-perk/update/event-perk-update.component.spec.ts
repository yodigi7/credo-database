jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventPerkService } from '../service/event-perk.service';
import { IEventPerk, EventPerk } from '../event-perk.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { EventPerkUpdateComponent } from './event-perk-update.component';

describe('Component Tests', () => {
  describe('EventPerk Management Update Component', () => {
    let comp: EventPerkUpdateComponent;
    let fixture: ComponentFixture<EventPerkUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventPerkService: EventPerkService;
    let eventService: EventService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventPerkUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventPerkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventPerkUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventPerkService = TestBed.inject(EventPerkService);
      eventService = TestBed.inject(EventService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Event query and add missing value', () => {
        const eventPerk: IEventPerk = { id: 456 };
        const event: IEvent = { id: 66877 };
        eventPerk.event = event;

        const eventCollection: IEvent[] = [{ id: 87517 }];
        spyOn(eventService, 'query').and.returnValue(of(new HttpResponse({ body: eventCollection })));
        const additionalEvents = [event];
        const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
        spyOn(eventService, 'addEventToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        expect(eventService.query).toHaveBeenCalled();
        expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
        expect(comp.eventsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const eventPerk: IEventPerk = { id: 456 };
        const person: IPerson = { id: 4804 };
        eventPerk.person = person;

        const personCollection: IPerson[] = [{ id: 51581 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const eventPerk: IEventPerk = { id: 456 };
        const event: IEvent = { id: 25375 };
        eventPerk.event = event;
        const person: IPerson = { id: 39584 };
        eventPerk.person = person;

        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(eventPerk));
        expect(comp.eventsSharedCollection).toContain(event);
        expect(comp.peopleSharedCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventPerk = { id: 123 };
        spyOn(eventPerkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: eventPerk }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventPerkService.update).toHaveBeenCalledWith(eventPerk);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventPerk = new EventPerk();
        spyOn(eventPerkService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: eventPerk }));
        saveSubject.complete();

        // THEN
        expect(eventPerkService.create).toHaveBeenCalledWith(eventPerk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const eventPerk = { id: 123 };
        spyOn(eventPerkService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ eventPerk });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventPerkService.update).toHaveBeenCalledWith(eventPerk);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEventById', () => {
        it('Should return tracked Event primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventById(0, entity);
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
    });
  });
});
