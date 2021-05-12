jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganizationAddressService } from '../service/organization-address.service';
import { IOrganizationAddress, OrganizationAddress } from '../organization-address.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrganizationAddressUpdateComponent } from './organization-address-update.component';

describe('Component Tests', () => {
  describe('OrganizationAddress Management Update Component', () => {
    let comp: OrganizationAddressUpdateComponent;
    let fixture: ComponentFixture<OrganizationAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organizationAddressService: OrganizationAddressService;
    let organizationService: OrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganizationAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganizationAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganizationAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organizationAddressService = TestBed.inject(OrganizationAddressService);
      organizationService = TestBed.inject(OrganizationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Organization query and add missing value', () => {
        const organizationAddress: IOrganizationAddress = { id: 456 };
        const organization: IOrganization = { id: 93174 };
        organizationAddress.organization = organization;

        const organizationCollection: IOrganization[] = [{ id: 78804 }];
        spyOn(organizationService, 'query').and.returnValue(of(new HttpResponse({ body: organizationCollection })));
        const additionalOrganizations = [organization];
        const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
        spyOn(organizationService, 'addOrganizationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ organizationAddress });
        comp.ngOnInit();

        expect(organizationService.query).toHaveBeenCalled();
        expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
          organizationCollection,
          ...additionalOrganizations
        );
        expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organizationAddress: IOrganizationAddress = { id: 456 };
        const organization: IOrganization = { id: 85278 };
        organizationAddress.organization = organization;

        activatedRoute.data = of({ organizationAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organizationAddress));
        expect(comp.organizationsSharedCollection).toContain(organization);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationAddress = { id: 123 };
        spyOn(organizationAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organizationAddressService.update).toHaveBeenCalledWith(organizationAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationAddress = new OrganizationAddress();
        spyOn(organizationAddressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organizationAddress }));
        saveSubject.complete();

        // THEN
        expect(organizationAddressService.create).toHaveBeenCalledWith(organizationAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const organizationAddress = { id: 123 };
        spyOn(organizationAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ organizationAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organizationAddressService.update).toHaveBeenCalledWith(organizationAddress);
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
