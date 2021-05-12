jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParish, Parish } from '../parish.model';
import { ParishService } from '../service/parish.service';

import { ParishRoutingResolveService } from './parish-routing-resolve.service';

describe('Service Tests', () => {
  describe('Parish routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ParishRoutingResolveService;
    let service: ParishService;
    let resultParish: IParish | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ParishRoutingResolveService);
      service = TestBed.inject(ParishService);
      resultParish = undefined;
    });

    describe('resolve', () => {
      it('should return IParish returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParish).toEqual({ id: 123 });
      });

      it('should return new IParish if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParish = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultParish).toEqual(new Parish());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParish).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
