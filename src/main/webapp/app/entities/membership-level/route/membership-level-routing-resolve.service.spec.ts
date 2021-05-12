jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMembershipLevel, MembershipLevel } from '../membership-level.model';
import { MembershipLevelService } from '../service/membership-level.service';

import { MembershipLevelRoutingResolveService } from './membership-level-routing-resolve.service';

describe('Service Tests', () => {
  describe('MembershipLevel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MembershipLevelRoutingResolveService;
    let service: MembershipLevelService;
    let resultMembershipLevel: IMembershipLevel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MembershipLevelRoutingResolveService);
      service = TestBed.inject(MembershipLevelService);
      resultMembershipLevel = undefined;
    });

    describe('resolve', () => {
      it('should return IMembershipLevel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMembershipLevel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMembershipLevel).toEqual({ id: 123 });
      });

      it('should return new IMembershipLevel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMembershipLevel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMembershipLevel).toEqual(new MembershipLevel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMembershipLevel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMembershipLevel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
