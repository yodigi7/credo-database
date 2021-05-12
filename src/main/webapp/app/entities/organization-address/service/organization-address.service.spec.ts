import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationAddress, OrganizationAddress } from '../organization-address.model';

import { OrganizationAddressService } from './organization-address.service';

describe('Service Tests', () => {
  describe('OrganizationAddress Service', () => {
    let service: OrganizationAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganizationAddress;
    let expectedResult: IOrganizationAddress | IOrganizationAddress[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganizationAddressService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        streetAddress: 'AAAAAAA',
        city: 'AAAAAAA',
        state: 'AAAAAAA',
        zipcode: 'AAAAAAA',
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

      it('should create a OrganizationAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrganizationAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganizationAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zipcode: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrganizationAddress', () => {
        const patchObject = Object.assign(
          {
            state: 'BBBBBB',
          },
          new OrganizationAddress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrganizationAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            streetAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zipcode: 'BBBBBB',
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

      it('should delete a OrganizationAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganizationAddressToCollectionIfMissing', () => {
        it('should add a OrganizationAddress to an empty array', () => {
          const organizationAddress: IOrganizationAddress = { id: 123 };
          expectedResult = service.addOrganizationAddressToCollectionIfMissing([], organizationAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationAddress);
        });

        it('should not add a OrganizationAddress to an array that contains it', () => {
          const organizationAddress: IOrganizationAddress = { id: 123 };
          const organizationAddressCollection: IOrganizationAddress[] = [
            {
              ...organizationAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganizationAddressToCollectionIfMissing(organizationAddressCollection, organizationAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganizationAddress to an array that doesn't contain it", () => {
          const organizationAddress: IOrganizationAddress = { id: 123 };
          const organizationAddressCollection: IOrganizationAddress[] = [{ id: 456 }];
          expectedResult = service.addOrganizationAddressToCollectionIfMissing(organizationAddressCollection, organizationAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationAddress);
        });

        it('should add only unique OrganizationAddress to an array', () => {
          const organizationAddressArray: IOrganizationAddress[] = [{ id: 123 }, { id: 456 }, { id: 38597 }];
          const organizationAddressCollection: IOrganizationAddress[] = [{ id: 123 }];
          expectedResult = service.addOrganizationAddressToCollectionIfMissing(organizationAddressCollection, ...organizationAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organizationAddress: IOrganizationAddress = { id: 123 };
          const organizationAddress2: IOrganizationAddress = { id: 456 };
          expectedResult = service.addOrganizationAddressToCollectionIfMissing([], organizationAddress, organizationAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationAddress);
          expect(expectedResult).toContain(organizationAddress2);
        });

        it('should accept null and undefined values', () => {
          const organizationAddress: IOrganizationAddress = { id: 123 };
          expectedResult = service.addOrganizationAddressToCollectionIfMissing([], null, organizationAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationAddress);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
