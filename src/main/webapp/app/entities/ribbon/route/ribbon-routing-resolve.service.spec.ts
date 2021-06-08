jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRibbon, Ribbon } from '../ribbon.model';
import { RibbonService } from '../service/ribbon.service';

import { RibbonRoutingResolveService } from './ribbon-routing-resolve.service';

describe('Service Tests', () => {
  describe('Ribbon routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RibbonRoutingResolveService;
    let service: RibbonService;
    let resultRibbon: IRibbon | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RibbonRoutingResolveService);
      service = TestBed.inject(RibbonService);
      resultRibbon = undefined;
    });

    describe('resolve', () => {
      it('should return IRibbon returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRibbon = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRibbon).toEqual({ id: 123 });
      });

      it('should return new IRibbon if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRibbon = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRibbon).toEqual(new Ribbon());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRibbon = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRibbon).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
