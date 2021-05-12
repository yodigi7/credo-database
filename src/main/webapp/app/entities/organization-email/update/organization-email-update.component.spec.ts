jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganizationEmailService } from '../service/organization-email.service';
import { IOrganizationEmail, OrganizationEmail } from '../organization-email.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrganizationEmailUpdateComponent } from './organization-email-update.component';

describe('Component Tests', () => {
  describe('OrganizationEmail Management Update Component', () => {
    let comp: OrganizationEmailUpdateComponent;
    let fixture: ComponentFixture<OrganizationEmailUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organizationEmailService: OrganizationEmailService;
    let organizationService: OrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganizationEmailUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganizationEmailUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganizationEmailUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organizationEmailService = TestBed.inject(OrganizationEmailService);
      organizationService = TestBed.inject(OrganizationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Organization query and add missing value', () => {
        const organizationEmail: IOrganizationEmail = { id: 456 };
        const organization: IOrganization = { id: 3880 };
        organizationEmail.organization = organization;

        const organizationCollection: IOrganization[] = [{ id: 42076 }];
        spyOn(organizationService, 'query').and.returnValue(of(new HttpResponse({ body: organizationCollection })));
        const additionalOrganizations = [organization];
        const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
        spyOn(organizationService, 'addOrganizationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ organizationEmail });
        comp.ngOnInit();

        expect(organizationService.query).toHaveBeenCalled();
        expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
          organizationCollection,
          ...additionalOrganizations
        );
        expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organizationEmail: IOrganizationEmail = { id: 456 };
        const organization: IOrganization = { id: 90070 };
        organizationEmail.organization = organization;

        activatedRoute.data = of({ organizationEmail });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organizationEmail));
        expect(comp.organizationsSharedCollection).toContain(organization);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationEmail = { id: 123 };
        spyOn(organizationEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationEmail }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organizationEmailService.update).toHaveBeenCalledWith(organizationEmail);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationEmail = new OrganizationEmail();
        spyOn(organizationEmailService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationEmail }));
        saveSubject.complete();

        // THEN
        expect(organizationEmailService.create).toHaveBeenCalledWith(organizationEmail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationEmail = { id: 123 };
        spyOn(organizationEmailService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationEmail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organizationEmailService.update).toHaveBeenCalledWith(organizationEmail);
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
