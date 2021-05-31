jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonService } from '../service/person.service';
import { IPerson, Person } from '../person.model';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { MembershipLevelService } from 'app/entities/membership-level/service/membership-level.service';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Component Tests', () => {
  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponent;
    let fixture: ComponentFixture<PersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;
    let membershipLevelService: MembershipLevelService;
    let parishService: ParishService;
    let organizationService: OrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);
      membershipLevelService = TestBed.inject(MembershipLevelService);
      parishService = TestBed.inject(ParishService);
      organizationService = TestBed.inject(OrganizationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const headOfHouse: IPerson = { id: 53468 };
        person.headOfHouse = headOfHouse;

        const personCollection: IPerson[] = [{ id: 32450 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [headOfHouse];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call spouse query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const spouse: IPerson = { id: 92101 };
        person.spouse = spouse;

        const spouseCollection: IPerson[] = [{ id: 1921 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: spouseCollection })));
        const expectedCollection: IPerson[] = [spouse, ...spouseCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(spouseCollection, spouse);
        expect(comp.spousesCollection).toEqual(expectedCollection);
      });

      it('Should call MembershipLevel query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const membershipLevel: IMembershipLevel = { id: 96705 };
        person.membershipLevel = membershipLevel;

        const membershipLevelCollection: IMembershipLevel[] = [{ id: 19380 }];
        spyOn(membershipLevelService, 'query').and.returnValue(of(new HttpResponse({ body: membershipLevelCollection })));
        const additionalMembershipLevels = [membershipLevel];
        const expectedCollection: IMembershipLevel[] = [...additionalMembershipLevels, ...membershipLevelCollection];
        spyOn(membershipLevelService, 'addMembershipLevelToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(membershipLevelService.query).toHaveBeenCalled();
        expect(membershipLevelService.addMembershipLevelToCollectionIfMissing).toHaveBeenCalledWith(
          membershipLevelCollection,
          ...additionalMembershipLevels
        );
        expect(comp.membershipLevelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Parish query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const parish: IParish = { id: 92785 };
        person.parish = parish;

        const parishCollection: IParish[] = [{ id: 88912 }];
        spyOn(parishService, 'query').and.returnValue(of(new HttpResponse({ body: parishCollection })));
        const additionalParishes = [parish];
        const expectedCollection: IParish[] = [...additionalParishes, ...parishCollection];
        spyOn(parishService, 'addParishToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(parishService.query).toHaveBeenCalled();
        expect(parishService.addParishToCollectionIfMissing).toHaveBeenCalledWith(parishCollection, ...additionalParishes);
        expect(comp.parishesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Organization query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const organizations: IOrganization[] = [{ id: 7594 }];
        person.organizations = organizations;

        const organizationCollection: IOrganization[] = [{ id: 15246 }];
        spyOn(organizationService, 'query').and.returnValue(of(new HttpResponse({ body: organizationCollection })));
        const additionalOrganizations = [...organizations];
        const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
        spyOn(organizationService, 'addOrganizationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(organizationService.query).toHaveBeenCalled();
        expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
          organizationCollection,
          ...additionalOrganizations
        );
        expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const person: IPerson = { id: 456 };
        const spouse: IPerson = { id: 40227 };
        person.spouse = spouse;
        const headOfHouse: IPerson = { id: 11579 };
        person.headOfHouse = headOfHouse;
        const membershipLevel: IMembershipLevel = { id: 21970 };
        person.membershipLevel = membershipLevel;
        const parish: IParish = { id: 82163 };
        person.parish = parish;
        const organizations: IOrganization = { id: 87375 };
        person.organizations = [organizations];

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(person));
        expect(comp.spousesCollection).toContain(spouse);
        expect(comp.peopleSharedCollection).toContain(headOfHouse);
        expect(comp.membershipLevelsSharedCollection).toContain(membershipLevel);
        expect(comp.parishesSharedCollection).toContain(parish);
        expect(comp.organizationsSharedCollection).toContain(organizations);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = new Person();
        spyOn(personService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(personService.create).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personService.update).toHaveBeenCalledWith(person);
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

      describe('trackMembershipLevelById', () => {
        it('Should return tracked MembershipLevel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMembershipLevelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackParishById', () => {
        it('Should return tracked Parish primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackParishById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrganizationById', () => {
        it('Should return tracked Organization primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganizationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOrganization', () => {
        it('Should return option if no Organization is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOrganization(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Organization for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOrganization(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Organization is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOrganization(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
