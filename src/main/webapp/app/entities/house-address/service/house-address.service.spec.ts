import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { IHouseAddress, HouseAddress } from '../house-address.model';

import { HouseAddressService } from './house-address.service';

describe('Service Tests', () => {
  describe('HouseAddress Service', () => {
    let service: HouseAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IHouseAddress;
    let expectedResult: IHouseAddress | IHouseAddress[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(HouseAddressService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        streetAddress: 'AAAAAAA',
        city: 'AAAAAAA',
        state: 'AAAAAAA',
        zipcode: 'AAAAAAA',
        type: 'AAAAAAA',
        mailNewsletterSubscription: YesNoEmpty.YES,
        mailEventNotificationSubscription: YesNoEmpty.YES,
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

      it('should create a HouseAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new HouseAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a HouseAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zipcode: 'BBBBBB',
            type: 'BBBBBB',
            mailNewsletterSubscription: 'BBBBBB',
            mailEventNotificationSubscription: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a HouseAddress', () => {
        const patchObject = Object.assign(
          {
            streetAddress: 'BBBBBB',
            state: 'BBBBBB',
            mailEventNotificationSubscription: 'BBBBBB',
          },
          new HouseAddress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of HouseAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zipcode: 'BBBBBB',
            type: 'BBBBBB',
            mailNewsletterSubscription: 'BBBBBB',
            mailEventNotificationSubscription: 'BBBBBB',
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

      it('should delete a HouseAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addHouseAddressToCollectionIfMissing', () => {
        it('should add a HouseAddress to an empty array', () => {
          const houseAddress: IHouseAddress = { id: 123 };
          expectedResult = service.addHouseAddressToCollectionIfMissing([], houseAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(houseAddress);
        });

        it('should not add a HouseAddress to an array that contains it', () => {
          const houseAddress: IHouseAddress = { id: 123 };
          const houseAddressCollection: IHouseAddress[] = [
            {
              ...houseAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addHouseAddressToCollectionIfMissing(houseAddressCollection, houseAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a HouseAddress to an array that doesn't contain it", () => {
          const houseAddress: IHouseAddress = { id: 123 };
          const houseAddressCollection: IHouseAddress[] = [{ id: 456 }];
          expectedResult = service.addHouseAddressToCollectionIfMissing(houseAddressCollection, houseAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(houseAddress);
        });

        it('should add only unique HouseAddress to an array', () => {
          const houseAddressArray: IHouseAddress[] = [{ id: 123 }, { id: 456 }, { id: 9652 }];
          const houseAddressCollection: IHouseAddress[] = [{ id: 123 }];
          expectedResult = service.addHouseAddressToCollectionIfMissing(houseAddressCollection, ...houseAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const houseAddress: IHouseAddress = { id: 123 };
          const houseAddress2: IHouseAddress = { id: 456 };
          expectedResult = service.addHouseAddressToCollectionIfMissing([], houseAddress, houseAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(houseAddress);
          expect(expectedResult).toContain(houseAddress2);
        });

        it('should accept null and undefined values', () => {
          const houseAddress: IHouseAddress = { id: 123 };
          expectedResult = service.addHouseAddressToCollectionIfMissing([], null, houseAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(houseAddress);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
