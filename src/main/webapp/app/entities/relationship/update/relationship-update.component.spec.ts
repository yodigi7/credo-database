jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RelationshipService } from '../service/relationship.service';
import { IRelationship, Relationship } from '../relationship.model';

import { RelationshipUpdateComponent } from './relationship-update.component';

describe('Component Tests', () => {
  describe('Relationship Management Update Component', () => {
    let comp: RelationshipUpdateComponent;
    let fixture: ComponentFixture<RelationshipUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let relationshipService: RelationshipService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelationshipUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RelationshipUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RelationshipUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      relationshipService = TestBed.inject(RelationshipService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const relationship: IRelationship = { id: 456 };

        activatedRoute.data = of({ relationship });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(relationship));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relationship = { id: 123 };
        spyOn(relationshipService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relationship });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relationship }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(relationshipService.update).toHaveBeenCalledWith(relationship);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relationship = new Relationship();
        spyOn(relationshipService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relationship });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relationship }));
        saveSubject.complete();

        // THEN
        expect(relationshipService.create).toHaveBeenCalledWith(relationship);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relationship = { id: 123 };
        spyOn(relationshipService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relationship });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(relationshipService.update).toHaveBeenCalledWith(relationship);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
