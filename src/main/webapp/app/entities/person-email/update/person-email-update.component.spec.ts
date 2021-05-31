jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonEmailService } from '../service/person-email.service';
import { IPersonEmail, PersonEmail } from '../person-email.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PersonEmailUpdateComponent } from './person-email-update.component';

describe('Component Tests', () => {
  describe('PersonEmail Management Update Component', () => {
    let comp: PersonEmailUpdateComponent;
    let fixture: ComponentFixture<PersonEmailUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personEmailService: PersonEmailService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonEmailUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonEmailUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonEmailUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personEmailService = TestBed.inject(PersonEmailService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        const personEmail: IPersonEmail = { id: 456 };
        const person: IPerson = { id: 96986 };
        personEmail.person = person;

        const personCollection: IPerson[] = [{ id: 53042 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ personEmail });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personEmail: IPersonEmail = { id: 456 };
        const person: IPerson = { id: 91147 };
        personEmail.person = person;

        activatedRoute.data = of({ personEmail });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personEmail));
        expect(comp.peopleSharedCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personEmail = { id: 123 };
        spyOn(personEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personEmail }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personEmailService.update).toHaveBeenCalledWith(personEmail);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personEmail = new PersonEmail();
        spyOn(personEmailService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personEmail }));
        saveSubject.complete();

        // THEN
        expect(personEmailService.create).toHaveBeenCalledWith(personEmail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personEmail = { id: 123 };
        spyOn(personEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personEmailService.update).toHaveBeenCalledWith(personEmail);
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
