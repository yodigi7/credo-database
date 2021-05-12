jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganizationPhoneService } from '../service/organization-phone.service';
import { IOrganizationPhone, OrganizationPhone } from '../organization-phone.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrganizationPhoneUpdateComponent } from './organization-phone-update.component';

describe('Component Tests', () => {
  describe('OrganizationPhone Management Update Component', () => {
    let comp: OrganizationPhoneUpdateComponent;
    let fixture: ComponentFixture<OrganizationPhoneUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organizationPhoneService: OrganizationPhoneService;
    let organizationService: OrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganizationPhoneUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganizationPhoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganizationPhoneUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organizationPhoneService = TestBed.inject(OrganizationPhoneService);
      organizationService = TestBed.inject(OrganizationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Organization query and add missing value', () => {
        const organizationPhone: IOrganizationPhone = { id: 456 };
        const organization: IOrganization = { id: 4124 };
        organizationPhone.organization = organization;

        const organizationCollection: IOrganization[] = [{ id: 26376 }];
        spyOn(organizationService, 'query').and.returnValue(of(new HttpResponse({ body: organizationCollection })));
        const additionalOrganizations = [organization];
        const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
        spyOn(organizationService, 'addOrganizationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ organizationPhone });
        comp.ngOnInit();

        expect(organizationService.query).toHaveBeenCalled();
        expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
          organizationCollection,
          ...additionalOrganizations
        );
        expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organizationPhone: IOrganizationPhone = { id: 456 };
        const organization: IOrganization = { id: 4455 };
        organizationPhone.organization = organization;

        activatedRoute.data = of({ organizationPhone });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organizationPhone));
        expect(comp.organizationsSharedCollection).toContain(organization);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationPhone = { id: 123 };
        spyOn(organizationPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationPhone }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organizationPhoneService.update).toHaveBeenCalledWith(organizationPhone);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationPhone = new OrganizationPhone();
        spyOn(organizationPhoneService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationPhone }));
        saveSubject.complete();

        // THEN
        expect(organizationPhoneService.create).toHaveBeenCalledWith(organizationPhone);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationPhone = { id: 123 };
        spyOn(organizationPhoneService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organizationPhoneService.update).toHaveBeenCalledWith(organizationPhone);
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
