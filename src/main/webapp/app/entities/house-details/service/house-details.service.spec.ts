import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHouseDetails, HouseDetails } from '../house-details.model';

import { HouseDetailsService } from './house-details.service';

describe('Service Tests', () => {
  describe('HouseDetails Service', () => {
    let service: HouseDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IHouseDetails;
    let expectedResult: IHouseDetails | IHouseDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(HouseDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        mailingLabel: 'AAAAAAA',
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

      it('should create a HouseDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new HouseDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a HouseDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mailingLabel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a HouseDetails', () => {
        const patchObject = Object.assign({}, new HouseDetails());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of HouseDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mailingLabel: 'BBBBBB',
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

      it('should delete a HouseDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addHouseDetailsToCollectionIfMissing', () => {
        it('should add a HouseDetails to an empty array', () => {
          const houseDetails: IHouseDetails = { id: 123 };
          expectedResult = service.addHouseDetailsToCollectionIfMissing([], houseDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(houseDetails);
        });

        it('should not add a HouseDetails to an array that contains it', () => {
          const houseDetails: IHouseDetails = { id: 123 };
          const houseDetailsCollection: IHouseDetails[] = [
            {
              ...houseDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addHouseDetailsToCollectionIfMissing(houseDetailsCollection, houseDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a HouseDetails to an array that doesn't contain it", () => {
          const houseDetails: IHouseDetails = { id: 123 };
          const houseDetailsCollection: IHouseDetails[] = [{ id: 456 }];
          expectedResult = service.addHouseDetailsToCollectionIfMissing(houseDetailsCollection, houseDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(houseDetails);
        });

        it('should add only unique HouseDetails to an array', () => {
          const houseDetailsArray: IHouseDetails[] = [{ id: 123 }, { id: 456 }, { id: 60711 }];
          const houseDetailsCollection: IHouseDetails[] = [{ id: 123 }];
          expectedResult = service.addHouseDetailsToCollectionIfMissing(houseDetailsCollection, ...houseDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const houseDetails: IHouseDetails = { id: 123 };
          const houseDetails2: IHouseDetails = { id: 456 };
          expectedResult = service.addHouseDetailsToCollectionIfMissing([], houseDetails, houseDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(houseDetails);
          expect(expectedResult).toContain(houseDetails2);
        });

        it('should accept null and undefined values', () => {
          const houseDetails: IHouseDetails = { id: 123 };
          expectedResult = service.addHouseDetailsToCollectionIfMissing([], null, houseDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(houseDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
