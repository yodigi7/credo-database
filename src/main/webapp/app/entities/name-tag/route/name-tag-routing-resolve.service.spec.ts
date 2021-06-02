jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INameTag, NameTag } from '../name-tag.model';
import { NameTagService } from '../service/name-tag.service';

import { NameTagRoutingResolveService } from './name-tag-routing-resolve.service';

describe('Service Tests', () => {
  describe('NameTag routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NameTagRoutingResolveService;
    let service: NameTagService;
    let resultNameTag: INameTag | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NameTagRoutingResolveService);
      service = TestBed.inject(NameTagService);
      resultNameTag = undefined;
    });

    describe('resolve', () => {
      it('should return INameTag returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNameTag = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNameTag).toEqual({ id: 123 });
      });

      it('should return new INameTag if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNameTag = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNameTag).toEqual(new NameTag());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNameTag = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNameTag).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
