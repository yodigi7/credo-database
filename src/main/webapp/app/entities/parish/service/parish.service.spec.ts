import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParish, Parish } from '../parish.model';

import { ParishService } from './parish.service';

describe('Service Tests', () => {
  describe('Parish Service', () => {
    let service: ParishService;
    let httpMock: HttpTestingController;
    let elemDefault: IParish;
    let expectedResult: IParish | IParish[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ParishService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a Parish', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Parish()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Parish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Parish', () => {
        const patchObject = Object.assign({}, new Parish());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Parish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a Parish', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addParishToCollectionIfMissing', () => {
        it('should add a Parish to an empty array', () => {
          const parish: IParish = { id: 123 };
          expectedResult = service.addParishToCollectionIfMissing([], parish);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parish);
        });

        it('should not add a Parish to an array that contains it', () => {
          const parish: IParish = { id: 123 };
          const parishCollection: IParish[] = [
            {
              ...parish,
            },
            { id: 456 },
          ];
          expectedResult = service.addParishToCollectionIfMissing(parishCollection, parish);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Parish to an array that doesn't contain it", () => {
          const parish: IParish = { id: 123 };
          const parishCollection: IParish[] = [{ id: 456 }];
          expectedResult = service.addParishToCollectionIfMissing(parishCollection, parish);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parish);
        });

        it('should add only unique Parish to an array', () => {
          const parishArray: IParish[] = [{ id: 123 }, { id: 456 }, { id: 56076 }];
          const parishCollection: IParish[] = [{ id: 123 }];
          expectedResult = service.addParishToCollectionIfMissing(parishCollection, ...parishArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const parish: IParish = { id: 123 };
          const parish2: IParish = { id: 456 };
          expectedResult = service.addParishToCollectionIfMissing([], parish, parish2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parish);
          expect(expectedResult).toContain(parish2);
        });

        it('should accept null and undefined values', () => {
          const parish: IParish = { id: 123 };
          expectedResult = service.addParishToCollectionIfMissing([], null, parish, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parish);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
