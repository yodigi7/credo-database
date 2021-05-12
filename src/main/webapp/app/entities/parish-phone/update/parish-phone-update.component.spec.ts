jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParishPhoneService } from '../service/parish-phone.service';
import { IParishPhone, ParishPhone } from '../parish-phone.model';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';

import { ParishPhoneUpdateComponent } from './parish-phone-update.component';

describe('Component Tests', () => {
  describe('ParishPhone Management Update Component', () => {
    let comp: ParishPhoneUpdateComponent;
    let fixture: ComponentFixture<ParishPhoneUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let parishPhoneService: ParishPhoneService;
    let parishService: ParishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParishPhoneUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ParishPhoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParishPhoneUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      parishPhoneService = TestBed.inject(ParishPhoneService);
      parishService = TestBed.inject(ParishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Parish query and add missing value', () => {
        const parishPhone: IParishPhone = { id: 456 };
        const parish: IParish = { id: 95061 };
        parishPhone.parish = parish;

        const parishCollection: IParish[] = [{ id: 34568 }];
        spyOn(parishService, 'query').and.returnValue(of(new HttpResponse({ body: parishCollection })));
        const additionalParishes = [parish];
        const expectedCollection: IParish[] = [...additionalParishes, ...parishCollection];
        spyOn(parishService, 'addParishToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ parishPhone });
        comp.ngOnInit();

        expect(parishService.query).toHaveBeenCalled();
        expect(parishService.addParishToCollectionIfMissing).toHaveBeenCalledWith(parishCollection, ...additionalParishes);
        expect(comp.parishesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const parishPhone: IParishPhone = { id: 456 };
        const parish: IParish = { id: 61376 };
        parishPhone.parish = parish;

        activatedRoute.data = of({ parishPhone });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(parishPhone));
        expect(comp.parishesSharedCollection).toContain(parish);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishPhone = { id: 123 };
        spyOn(parishPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parishPhone }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(parishPhoneService.update).toHaveBeenCalledWith(parishPhone);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishPhone = new ParishPhone();
        spyOn(parishPhoneService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parishPhone }));
        saveSubject.complete();

        // THEN
        expect(parishPhoneService.create).toHaveBeenCalledWith(parishPhone);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishPhone = { id: 123 };
        spyOn(parishPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(parishPhoneService.update).toHaveBeenCalledWith(parishPhone);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackParishById', () => {
        it('Should return tracked Parish primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackParishById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
