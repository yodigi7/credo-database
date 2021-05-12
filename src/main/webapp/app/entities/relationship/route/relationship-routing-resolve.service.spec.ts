jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRelationship, Relationship } from '../relationship.model';
import { RelationshipService } from '../service/relationship.service';

import { RelationshipRoutingResolveService } from './relationship-routing-resolve.service';

describe('Service Tests', () => {
  describe('Relationship routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RelationshipRoutingResolveService;
    let service: RelationshipService;
    let resultRelationship: IRelationship | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RelationshipRoutingResolveService);
      service = TestBed.inject(RelationshipService);
      resultRelationship = undefined;
    });

    describe('resolve', () => {
      it('should return IRelationship returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelationship = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelationship).toEqual({ id: 123 });
      });

      it('should return new IRelationship if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelationship = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRelationship).toEqual(new Relationship());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelationship = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelationship).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
