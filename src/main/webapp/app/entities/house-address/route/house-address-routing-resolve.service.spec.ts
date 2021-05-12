jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHouseAddress, HouseAddress } from '../house-address.model';
import { HouseAddressService } from '../service/house-address.service';

import { HouseAddressRoutingResolveService } from './house-address-routing-resolve.service';

describe('Service Tests', () => {
  describe('HouseAddress routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: HouseAddressRoutingResolveService;
    let service: HouseAddressService;
    let resultHouseAddress: IHouseAddress | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(HouseAddressRoutingResolveService);
      service = TestBed.inject(HouseAddressService);
      resultHouseAddress = undefined;
    });

    describe('resolve', () => {
      it('should return IHouseAddress returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHouseAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHouseAddress).toEqual({ id: 123 });
      });

      it('should return new IHouseAddress if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHouseAddress = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultHouseAddress).toEqual(new HouseAddress());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHouseAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHouseAddress).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
