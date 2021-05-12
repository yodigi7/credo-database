jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MembershipLevelService } from '../service/membership-level.service';
import { IMembershipLevel, MembershipLevel } from '../membership-level.model';

import { MembershipLevelUpdateComponent } from './membership-level-update.component';

describe('Component Tests', () => {
  describe('MembershipLevel Management Update Component', () => {
    let comp: MembershipLevelUpdateComponent;
    let fixture: ComponentFixture<MembershipLevelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let membershipLevelService: MembershipLevelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MembershipLevelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MembershipLevelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembershipLevelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      membershipLevelService = TestBed.inject(MembershipLevelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const membershipLevel: IMembershipLevel = { id: 456 };

        activatedRoute.data = of({ membershipLevel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(membershipLevel));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membershipLevel = { id: 123 };
        spyOn(membershipLevelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membershipLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membershipLevel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(membershipLevelService.update).toHaveBeenCalledWith(membershipLevel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membershipLevel = new MembershipLevel();
        spyOn(membershipLevelService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membershipLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membershipLevel }));
        saveSubject.complete();

        // THEN
        expect(membershipLevelService.create).toHaveBeenCalledWith(membershipLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membershipLevel = { id: 123 };
        spyOn(membershipLevelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membershipLevel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(membershipLevelService.update).toHaveBeenCalledWith(membershipLevel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
