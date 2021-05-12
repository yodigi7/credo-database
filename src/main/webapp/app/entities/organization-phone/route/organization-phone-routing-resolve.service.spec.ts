jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrganizationPhone, OrganizationPhone } from '../organization-phone.model';
import { OrganizationPhoneService } from '../service/organization-phone.service';

import { OrganizationPhoneRoutingResolveService } from './organization-phone-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrganizationPhone routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrganizationPhoneRoutingResolveService;
    let service: OrganizationPhoneService;
    let resultOrganizationPhone: IOrganizationPhone | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrganizationPhoneRoutingResolveService);
      service = TestBed.inject(OrganizationPhoneService);
      resultOrganizationPhone = undefined;
    });

    describe('resolve', () => {
      it('should return IOrganizationPhone returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganizationPhone).toEqual({ id: 123 });
      });

      it('should return new IOrganizationPhone if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationPhone = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrganizationPhone).toEqual(new OrganizationPhone());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganizationPhone).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
