jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParishEmailService } from '../service/parish-email.service';
import { IParishEmail, ParishEmail } from '../parish-email.model';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';

import { ParishEmailUpdateComponent } from './parish-email-update.component';

describe('Component Tests', () => {
  describe('ParishEmail Management Update Component', () => {
    let comp: ParishEmailUpdateComponent;
    let fixture: ComponentFixture<ParishEmailUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let parishEmailService: ParishEmailService;
    let parishService: ParishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParishEmailUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ParishEmailUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParishEmailUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      parishEmailService = TestBed.inject(ParishEmailService);
      parishService = TestBed.inject(ParishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Parish query and add missing value', () => {
        const parishEmail: IParishEmail = { id: 456 };
        const parish: IParish = { id: 63343 };
        parishEmail.parish = parish;

        const parishCollection: IParish[] = [{ id: 54275 }];
        spyOn(parishService, 'query').and.returnValue(of(new HttpResponse({ body: parishCollection })));
        const additionalParishes = [parish];
        const expectedCollection: IParish[] = [...additionalParishes, ...parishCollection];
        spyOn(parishService, 'addParishToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ parishEmail });
        comp.ngOnInit();

        expect(parishService.query).toHaveBeenCalled();
        expect(parishService.addParishToCollectionIfMissing).toHaveBeenCalledWith(parishCollection, ...additionalParishes);
        expect(comp.parishesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const parishEmail: IParishEmail = { id: 456 };
        const parish: IParish = { id: 28594 };
        parishEmail.parish = parish;

        activatedRoute.data = of({ parishEmail });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(parishEmail));
        expect(comp.parishesSharedCollection).toContain(parish);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishEmail = { id: 123 };
        spyOn(parishEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parishEmail }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(parishEmailService.update).toHaveBeenCalledWith(parishEmail);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishEmail = new ParishEmail();
        spyOn(parishEmailService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parishEmail }));
        saveSubject.complete();

        // THEN
        expect(parishEmailService.create).toHaveBeenCalledWith(parishEmail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parishEmail = { id: 123 };
        spyOn(parishEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parishEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(parishEmailService.update).toHaveBeenCalledWith(parishEmail);
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
