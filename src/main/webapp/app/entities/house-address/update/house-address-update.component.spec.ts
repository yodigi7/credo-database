jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HouseAddressService } from '../service/house-address.service';
import { IHouseAddress, HouseAddress } from '../house-address.model';
import { IHouseDetails } from 'app/entities/house-details/house-details.model';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';

import { HouseAddressUpdateComponent } from './house-address-update.component';

describe('Component Tests', () => {
  describe('HouseAddress Management Update Component', () => {
    let comp: HouseAddressUpdateComponent;
    let fixture: ComponentFixture<HouseAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let houseAddressService: HouseAddressService;
    let houseDetailsService: HouseDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HouseAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HouseAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HouseAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      houseAddressService = TestBed.inject(HouseAddressService);
      houseDetailsService = TestBed.inject(HouseDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call HouseDetails query and add missing value', () => {
        const houseAddress: IHouseAddress = { id: 456 };
        const houseDetails: IHouseDetails = { id: 3012 };
        houseAddress.houseDetails = houseDetails;

        const houseDetailsCollection: IHouseDetails[] = [{ id: 9033 }];
        spyOn(houseDetailsService, 'query').and.returnValue(of(new HttpResponse({ body: houseDetailsCollection })));
        const additionalHouseDetails = [houseDetails];
        const expectedCollection: IHouseDetails[] = [...additionalHouseDetails, ...houseDetailsCollection];
        spyOn(houseDetailsService, 'addHouseDetailsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ houseAddress });
        comp.ngOnInit();

        expect(houseDetailsService.query).toHaveBeenCalled();
        expect(houseDetailsService.addHouseDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          houseDetailsCollection,
          ...additionalHouseDetails
        );
        expect(comp.houseDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const houseAddress: IHouseAddress = { id: 456 };
        const houseDetails: IHouseDetails = { id: 47386 };
        houseAddress.houseDetails = houseDetails;

        activatedRoute.data = of({ houseAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(houseAddress));
        expect(comp.houseDetailsSharedCollection).toContain(houseDetails);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseAddress = { id: 123 };
        spyOn(houseAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: houseAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(houseAddressService.update).toHaveBeenCalledWith(houseAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseAddress = new HouseAddress();
        spyOn(houseAddressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: houseAddress }));
        saveSubject.complete();

        // THEN
        expect(houseAddressService.create).toHaveBeenCalledWith(houseAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const houseAddress = { id: 123 };
        spyOn(houseAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ houseAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(houseAddressService.update).toHaveBeenCalledWith(houseAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackHouseDetailsById', () => {
        it('Should return tracked HouseDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackHouseDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
