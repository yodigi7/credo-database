jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParishPhone, ParishPhone } from '../parish-phone.model';
import { ParishPhoneService } from '../service/parish-phone.service';

import { ParishPhoneRoutingResolveService } from './parish-phone-routing-resolve.service';

describe('Service Tests', () => {
  describe('ParishPhone routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ParishPhoneRoutingResolveService;
    let service: ParishPhoneService;
    let resultParishPhone: IParishPhone | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ParishPhoneRoutingResolveService);
      service = TestBed.inject(ParishPhoneService);
      resultParishPhone = undefined;
    });

    describe('resolve', () => {
      it('should return IParishPhone returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParishPhone).toEqual({ id: 123 });
      });

      it('should return new IParishPhone if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishPhone = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultParishPhone).toEqual(new ParishPhone());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParishPhone).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
