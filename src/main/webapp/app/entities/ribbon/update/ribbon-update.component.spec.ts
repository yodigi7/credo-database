jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RibbonService } from '../service/ribbon.service';
import { IRibbon, Ribbon } from '../ribbon.model';

import { RibbonUpdateComponent } from './ribbon-update.component';

describe('Component Tests', () => {
  describe('Ribbon Management Update Component', () => {
    let comp: RibbonUpdateComponent;
    let fixture: ComponentFixture<RibbonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ribbonService: RibbonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RibbonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RibbonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RibbonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ribbonService = TestBed.inject(RibbonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ribbon: IRibbon = { id: 456 };

        activatedRoute.data = of({ ribbon });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ribbon));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ribbon = { id: 123 };
        spyOn(ribbonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ribbon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ribbon }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ribbonService.update).toHaveBeenCalledWith(ribbon);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ribbon = new Ribbon();
        spyOn(ribbonService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ribbon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ribbon }));
        saveSubject.complete();

        // THEN
        expect(ribbonService.create).toHaveBeenCalledWith(ribbon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ribbon = { id: 123 };
        spyOn(ribbonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ribbon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ribbonService.update).toHaveBeenCalledWith(ribbon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
