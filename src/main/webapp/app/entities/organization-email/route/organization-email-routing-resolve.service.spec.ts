jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrganizationEmail, OrganizationEmail } from '../organization-email.model';
import { OrganizationEmailService } from '../service/organization-email.service';

import { OrganizationEmailRoutingResolveService } from './organization-email-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrganizationEmail routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrganizationEmailRoutingResolveService;
    let service: OrganizationEmailService;
    let resultOrganizationEmail: IOrganizationEmail | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrganizationEmailRoutingResolveService);
      service = TestBed.inject(OrganizationEmailService);
      resultOrganizationEmail = undefined;
    });

    describe('resolve', () => {
      it('should return IOrganizationEmail returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationEmail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganizationEmail).toEqual({ id: 123 });
      });

      it('should return new IOrganizationEmail if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationEmail = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrganizationEmail).toEqual(new OrganizationEmail());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrganizationEmail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrganizationEmail).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
