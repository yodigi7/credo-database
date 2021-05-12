jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonPhone, PersonPhone } from '../person-phone.model';
import { PersonPhoneService } from '../service/person-phone.service';

import { PersonPhoneRoutingResolveService } from './person-phone-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonPhone routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonPhoneRoutingResolveService;
    let service: PersonPhoneService;
    let resultPersonPhone: IPersonPhone | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonPhoneRoutingResolveService);
      service = TestBed.inject(PersonPhoneService);
      resultPersonPhone = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonPhone returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonPhone).toEqual({ id: 123 });
      });

      it('should return new IPersonPhone if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonPhone = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonPhone).toEqual(new PersonPhone());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonPhone).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
