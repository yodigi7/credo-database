import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventPerk, EventPerk } from '../event-perk.model';

import { EventPerkService } from './event-perk.service';

describe('Service Tests', () => {
  describe('EventPerk Service', () => {
    let service: EventPerkService;
    let httpMock: HttpTestingController;
    let elemDefault: IEventPerk;
    let expectedResult: IEventPerk | IEventPerk[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EventPerkService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        minimumPrice: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EventPerk', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EventPerk()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EventPerk', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            minimumPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EventPerk', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new EventPerk()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EventPerk', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            minimumPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EventPerk', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEventPerkToCollectionIfMissing', () => {
        it('should add a EventPerk to an empty array', () => {
          const eventPerk: IEventPerk = { id: 123 };
          expectedResult = service.addEventPerkToCollectionIfMissing([], eventPerk);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(eventPerk);
        });

        it('should not add a EventPerk to an array that contains it', () => {
          const eventPerk: IEventPerk = { id: 123 };
          const eventPerkCollection: IEventPerk[] = [
            {
              ...eventPerk,
            },
            { id: 456 },
          ];
          expectedResult = service.addEventPerkToCollectionIfMissing(eventPerkCollection, eventPerk);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EventPerk to an array that doesn't contain it", () => {
          const eventPerk: IEventPerk = { id: 123 };
          const eventPerkCollection: IEventPerk[] = [{ id: 456 }];
          expectedResult = service.addEventPerkToCollectionIfMissing(eventPerkCollection, eventPerk);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(eventPerk);
        });

        it('should add only unique EventPerk to an array', () => {
          const eventPerkArray: IEventPerk[] = [{ id: 123 }, { id: 456 }, { id: 81299 }];
          const eventPerkCollection: IEventPerk[] = [{ id: 123 }];
          expectedResult = service.addEventPerkToCollectionIfMissing(eventPerkCollection, ...eventPerkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const eventPerk: IEventPerk = { id: 123 };
          const eventPerk2: IEventPerk = { id: 456 };
          expectedResult = service.addEventPerkToCollectionIfMissing([], eventPerk, eventPerk2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(eventPerk);
          expect(expectedResult).toContain(eventPerk2);
        });

        it('should accept null and undefined values', () => {
          const eventPerk: IEventPerk = { id: 123 };
          expectedResult = service.addEventPerkToCollectionIfMissing([], null, eventPerk, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(eventPerk);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
