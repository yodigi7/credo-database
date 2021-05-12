jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonPhoneService } from '../service/person-phone.service';
import { IPersonPhone, PersonPhone } from '../person-phone.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PersonPhoneUpdateComponent } from './person-phone-update.component';

describe('Component Tests', () => {
  describe('PersonPhone Management Update Component', () => {
    let comp: PersonPhoneUpdateComponent;
    let fixture: ComponentFixture<PersonPhoneUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personPhoneService: PersonPhoneService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonPhoneUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonPhoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonPhoneUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personPhoneService = TestBed.inject(PersonPhoneService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        const personPhone: IPersonPhone = { id: 456 };
        const person: IPerson = { id: 41066 };
        personPhone.person = person;

        const personCollection: IPerson[] = [{ id: 46605 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ personPhone });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personPhone: IPersonPhone = { id: 456 };
        const person: IPerson = { id: 64990 };
        personPhone.person = person;

        activatedRoute.data = of({ personPhone });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personPhone));
        expect(comp.peopleSharedCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personPhone = { id: 123 };
        spyOn(personPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personPhone }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personPhoneService.update).toHaveBeenCalledWith(personPhone);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personPhone = new PersonPhone();
        spyOn(personPhoneService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personPhone }));
        saveSubject.complete();

        // THEN
        expect(personPhoneService.create).toHaveBeenCalledWith(personPhone);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personPhone = { id: 123 };
        spyOn(personPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personPhoneService.update).toHaveBeenCalledWith(personPhone);
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
