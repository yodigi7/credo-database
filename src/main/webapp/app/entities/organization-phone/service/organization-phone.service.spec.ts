import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationPhone, OrganizationPhone } from '../organization-phone.model';

import { OrganizationPhoneService } from './organization-phone.service';

describe('Service Tests', () => {
  describe('OrganizationPhone Service', () => {
    let service: OrganizationPhoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganizationPhone;
    let expectedResult: IOrganizationPhone | IOrganizationPhone[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganizationPhoneService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        phoneNumber: 'AAAAAAA',
        type: 'AAAAAAA',
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

      it('should create a OrganizationPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrganizationPhone()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganizationPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            phoneNumber: 'BBBBBB',
            type: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrganizationPhone', () => {
        const patchObject = Object.assign(
          {
            phoneNumber: 'BBBBBB',
            type: 'BBBBBB',
          },
          new OrganizationPhone()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrganizationPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            phoneNumber: 'BBBBBB',
            type: 'BBBBBB',
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

      it('should delete a OrganizationPhone', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganizationPhoneToCollectionIfMissing', () => {
        it('should add a OrganizationPhone to an empty array', () => {
          const organizationPhone: IOrganizationPhone = { id: 123 };
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing([], organizationPhone);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationPhone);
        });

        it('should not add a OrganizationPhone to an array that contains it', () => {
          const organizationPhone: IOrganizationPhone = { id: 123 };
          const organizationPhoneCollection: IOrganizationPhone[] = [
            {
              ...organizationPhone,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing(organizationPhoneCollection, organizationPhone);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganizationPhone to an array that doesn't contain it", () => {
          const organizationPhone: IOrganizationPhone = { id: 123 };
          const organizationPhoneCollection: IOrganizationPhone[] = [{ id: 456 }];
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing(organizationPhoneCollection, organizationPhone);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationPhone);
        });

        it('should add only unique OrganizationPhone to an array', () => {
          const organizationPhoneArray: IOrganizationPhone[] = [{ id: 123 }, { id: 456 }, { id: 17825 }];
          const organizationPhoneCollection: IOrganizationPhone[] = [{ id: 123 }];
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing(organizationPhoneCollection, ...organizationPhoneArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organizationPhone: IOrganizationPhone = { id: 123 };
          const organizationPhone2: IOrganizationPhone = { id: 456 };
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing([], organizationPhone, organizationPhone2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationPhone);
          expect(expectedResult).toContain(organizationPhone2);
        });

        it('should accept null and undefined values', () => {
          const organizationPhone: IOrganizationPhone = { id: 123 };
          expectedResult = service.addOrganizationPhoneToCollectionIfMissing([], null, organizationPhone, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationPhone);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
