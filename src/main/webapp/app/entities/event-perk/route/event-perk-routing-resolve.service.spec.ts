jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEventPerk, EventPerk } from '../event-perk.model';
import { EventPerkService } from '../service/event-perk.service';

import { EventPerkRoutingResolveService } from './event-perk-routing-resolve.service';

describe('Service Tests', () => {
  describe('EventPerk routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EventPerkRoutingResolveService;
    let service: EventPerkService;
    let resultEventPerk: IEventPerk | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EventPerkRoutingResolveService);
      service = TestBed.inject(EventPerkService);
      resultEventPerk = undefined;
    });

    describe('resolve', () => {
      it('should return IEventPerk returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEventPerk = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEventPerk).toEqual({ id: 123 });
      });

      it('should return new IEventPerk if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEventPerk = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEventPerk).toEqual(new EventPerk());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEventPerk = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEventPerk).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
