jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParishEmail, ParishEmail } from '../parish-email.model';
import { ParishEmailService } from '../service/parish-email.service';

import { ParishEmailRoutingResolveService } from './parish-email-routing-resolve.service';

describe('Service Tests', () => {
  describe('ParishEmail routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ParishEmailRoutingResolveService;
    let service: ParishEmailService;
    let resultParishEmail: IParishEmail | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ParishEmailRoutingResolveService);
      service = TestBed.inject(ParishEmailService);
      resultParishEmail = undefined;
    });

    describe('resolve', () => {
      it('should return IParishEmail returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishEmail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParishEmail).toEqual({ id: 123 });
      });

      it('should return new IParishEmail if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishEmail = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultParishEmail).toEqual(new ParishEmail());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParishEmail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParishEmail).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
