jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HouseDetailsService } from '../service/house-details.service';
import { IHouseDetails, HouseDetails } from '../house-details.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { HouseDetailsUpdateComponent } from './house-details-update.component';

describe('Component Tests', () => {
  describe('HouseDetails Management Update Component', () => {
    let comp: HouseDetailsUpdateComponent;
    let fixture: ComponentFixture<HouseDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let houseDetailsService: HouseDetailsService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HouseDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HouseDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HouseDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      houseDetailsService = TestBed.inject(HouseDetailsService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call headOfHouse query and add missing value', () => {
        const houseDetails: IHouseDetails = { id: 456 };
        const headOfHouse: IPerson = { id: 20012 };
        houseDetails.headOfHouse = headOfHouse;

        const headOfHouseCollection: IPerson[] = [{ id: 92077 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: headOfHouseCollection })));
        const expectedCollection: IPerson[] = [headOfHouse, ...headOfHouseCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ houseDetails });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(headOfHouseCollection, headOfHouse);
        expect(comp.headOfHousesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const houseDetails: IHouseDetails = { id: 456 };
        const headOfHouse: IPerson = { id: 87853 };
        houseDetails.headOfHouse = headOfHouse;

        activatedRoute.data = of({ houseDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(houseDetails));
        expect(comp.headOfHousesCollection).toContain(headOfHouse);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseDetails = { id: 123 };
        spyOn(houseDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: houseDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(houseDetailsService.update).toHaveBeenCalledWith(houseDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseDetails = new HouseDetails();
        spyOn(houseDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: houseDetails }));
        saveSubject.complete();

        // THEN
        expect(houseDetailsService.create).toHaveBeenCalledWith(houseDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseDetails = { id: 123 };
        spyOn(houseDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(houseDetailsService.update).toHaveBeenCalledWith(houseDetails);
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
