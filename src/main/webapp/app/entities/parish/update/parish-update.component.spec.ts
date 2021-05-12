jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParishService } from '../service/parish.service';
import { IParish, Parish } from '../parish.model';

import { ParishUpdateComponent } from './parish-update.component';

describe('Component Tests', () => {
  describe('Parish Management Update Component', () => {
    let comp: ParishUpdateComponent;
    let fixture: ComponentFixture<ParishUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let parishService: ParishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParishUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ParishUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParishUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      parishService = TestBed.inject(ParishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const parish: IParish = { id: 456 };

        activatedRoute.data = of({ parish });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(parish));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parish = { id: 123 };
        spyOn(parishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parish }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(parishService.update).toHaveBeenCalledWith(parish);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parish = new Parish();
        spyOn(parishService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parish }));
        saveSubject.complete();

        // THEN
        expect(parishService.create).toHaveBeenCalledWith(parish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const parish = { id: 123 };
        spyOn(parishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ parish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(parishService.update).toHaveBeenCalledWith(parish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
