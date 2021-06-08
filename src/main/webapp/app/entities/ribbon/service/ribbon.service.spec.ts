import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRibbon, Ribbon } from '../ribbon.model';

import { RibbonService } from './ribbon.service';

describe('Service Tests', () => {
  describe('Ribbon Service', () => {
    let service: RibbonService;
    let httpMock: HttpTestingController;
    let elemDefault: IRibbon;
    let expectedResult: IRibbon | IRibbon[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RibbonService);
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

      it('should create a Ribbon', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ribbon()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ribbon', () => {
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

      it('should partial update a Ribbon', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Ribbon()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ribbon', () => {
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

      it('should delete a Ribbon', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRibbonToCollectionIfMissing', () => {
        it('should add a Ribbon to an empty array', () => {
          const ribbon: IRibbon = { id: 123 };
          expectedResult = service.addRibbonToCollectionIfMissing([], ribbon);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ribbon);
        });

        it('should not add a Ribbon to an array that contains it', () => {
          const ribbon: IRibbon = { id: 123 };
          const ribbonCollection: IRibbon[] = [
            {
              ...ribbon,
            },
            { id: 456 },
          ];
          expectedResult = service.addRibbonToCollectionIfMissing(ribbonCollection, ribbon);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ribbon to an array that doesn't contain it", () => {
          const ribbon: IRibbon = { id: 123 };
          const ribbonCollection: IRibbon[] = [{ id: 456 }];
          expectedResult = service.addRibbonToCollectionIfMissing(ribbonCollection, ribbon);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ribbon);
        });

        it('should add only unique Ribbon to an array', () => {
          const ribbonArray: IRibbon[] = [{ id: 123 }, { id: 456 }, { id: 4608 }];
          const ribbonCollection: IRibbon[] = [{ id: 123 }];
          expectedResult = service.addRibbonToCollectionIfMissing(ribbonCollection, ...ribbonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ribbon: IRibbon = { id: 123 };
          const ribbon2: IRibbon = { id: 456 };
          expectedResult = service.addRibbonToCollectionIfMissing([], ribbon, ribbon2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ribbon);
          expect(expectedResult).toContain(ribbon2);
        });

        it('should accept null and undefined values', () => {
          const ribbon: IRibbon = { id: 123 };
          expectedResult = service.addRibbonToCollectionIfMissing([], null, ribbon, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ribbon);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
