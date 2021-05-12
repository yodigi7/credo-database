jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonNotes, PersonNotes } from '../person-notes.model';
import { PersonNotesService } from '../service/person-notes.service';

import { PersonNotesRoutingResolveService } from './person-notes-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonNotes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonNotesRoutingResolveService;
    let service: PersonNotesService;
    let resultPersonNotes: IPersonNotes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonNotesRoutingResolveService);
      service = TestBed.inject(PersonNotesService);
      resultPersonNotes = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonNotes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonNotes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonNotes).toEqual({ id: 123 });
      });

      it('should return new IPersonNotes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonNotes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonNotes).toEqual(new PersonNotes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonNotes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonNotes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
