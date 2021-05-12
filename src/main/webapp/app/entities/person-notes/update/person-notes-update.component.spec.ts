jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonNotesService } from '../service/person-notes.service';
import { IPersonNotes, PersonNotes } from '../person-notes.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PersonNotesUpdateComponent } from './person-notes-update.component';

describe('Component Tests', () => {
  describe('PersonNotes Management Update Component', () => {
    let comp: PersonNotesUpdateComponent;
    let fixture: ComponentFixture<PersonNotesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personNotesService: PersonNotesService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonNotesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonNotesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonNotesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personNotesService = TestBed.inject(PersonNotesService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call person query and add missing value', () => {
        const personNotes: IPersonNotes = { id: 456 };
        const person: IPerson = { id: 21378 };
        personNotes.person = person;

        const personCollection: IPerson[] = [{ id: 91829 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const expectedCollection: IPerson[] = [person, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ personNotes });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
        expect(comp.peopleCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personNotes: IPersonNotes = { id: 456 };
        const person: IPerson = { id: 68368 };
        personNotes.person = person;

        activatedRoute.data = of({ personNotes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personNotes));
        expect(comp.peopleCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personNotes = { id: 123 };
        spyOn(personNotesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personNotes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personNotesService.update).toHaveBeenCalledWith(personNotes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personNotes = new PersonNotes();
        spyOn(personNotesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personNotes }));
        saveSubject.complete();

        // THEN
        expect(personNotesService.create).toHaveBeenCalledWith(personNotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personNotes = { id: 123 };
        spyOn(personNotesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personNotesService.update).toHaveBeenCalledWith(personNotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
