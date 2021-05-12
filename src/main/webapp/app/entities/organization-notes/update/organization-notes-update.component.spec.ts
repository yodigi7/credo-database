jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganizationNotesService } from '../service/organization-notes.service';
import { IOrganizationNotes, OrganizationNotes } from '../organization-notes.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrganizationNotesUpdateComponent } from './organization-notes-update.component';

describe('Component Tests', () => {
  describe('OrganizationNotes Management Update Component', () => {
    let comp: OrganizationNotesUpdateComponent;
    let fixture: ComponentFixture<OrganizationNotesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organizationNotesService: OrganizationNotesService;
    let organizationService: OrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganizationNotesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganizationNotesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganizationNotesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organizationNotesService = TestBed.inject(OrganizationNotesService);
      organizationService = TestBed.inject(OrganizationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call organization query and add missing value', () => {
        const organizationNotes: IOrganizationNotes = { id: 456 };
        const organization: IOrganization = { id: 65909 };
        organizationNotes.organization = organization;

        const organizationCollection: IOrganization[] = [{ id: 63203 }];
        spyOn(organizationService, 'query').and.returnValue(of(new HttpResponse({ body: organizationCollection })));
        const expectedCollection: IOrganization[] = [organization, ...organizationCollection];
        spyOn(organizationService, 'addOrganizationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ organizationNotes });
        comp.ngOnInit();

        expect(organizationService.query).toHaveBeenCalled();
        expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(organizationCollection, organization);
        expect(comp.organizationsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organizationNotes: IOrganizationNotes = { id: 456 };
        const organization: IOrganization = { id: 27779 };
        organizationNotes.organization = organization;

        activatedRoute.data = of({ organizationNotes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organizationNotes));
        expect(comp.organizationsCollection).toContain(organization);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationNotes = { id: 123 };
        spyOn(organizationNotesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationNotes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organizationNotesService.update).toHaveBeenCalledWith(organizationNotes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationNotes = new OrganizationNotes();
        spyOn(organizationNotesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationNotes }));
        saveSubject.complete();

        // THEN
        expect(organizationNotesService.create).toHaveBeenCalledWith(organizationNotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationNotes = { id: 123 };
        spyOn(organizationNotesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationNotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organizationNotesService.update).toHaveBeenCalledWith(organizationNotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrganizationById', () => {
        it('Should return tracked Organization primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganizationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
