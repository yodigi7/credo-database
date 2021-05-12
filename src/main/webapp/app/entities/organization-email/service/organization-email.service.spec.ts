import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { IOrganizationEmail, OrganizationEmail } from '../organization-email.model';

import { OrganizationEmailService } from './organization-email.service';

describe('Service Tests', () => {
  describe('OrganizationEmail Service', () => {
    let service: OrganizationEmailService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganizationEmail;
    let expectedResult: IOrganizationEmail | IOrganizationEmail[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganizationEmailService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        email: 'AAAAAAA',
        type: 'AAAAAAA',
        emailNewsletterSubscription: YesNoEmpty.YES,
        emailEventNotificationSubscription: YesNoEmpty.YES,
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

      it('should create a OrganizationEmail', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrganizationEmail()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganizationEmail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            email: 'BBBBBB',
            type: 'BBBBBB',
            emailNewsletterSubscription: 'BBBBBB',
            emailEventNotificationSubscription: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrganizationEmail', () => {
        const patchObject = Object.assign(
          {
            email: 'BBBBBB',
          },
          new OrganizationEmail()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrganizationEmail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            email: 'BBBBBB',
            type: 'BBBBBB',
            emailNewsletterSubscription: 'BBBBBB',
            emailEventNotificationSubscription: 'BBBBBB',
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

      it('should delete a OrganizationEmail', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganizationEmailToCollectionIfMissing', () => {
        it('should add a OrganizationEmail to an empty array', () => {
          const organizationEmail: IOrganizationEmail = { id: 123 };
          expectedResult = service.addOrganizationEmailToCollectionIfMissing([], organizationEmail);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationEmail);
        });

        it('should not add a OrganizationEmail to an array that contains it', () => {
          const organizationEmail: IOrganizationEmail = { id: 123 };
          const organizationEmailCollection: IOrganizationEmail[] = [
            {
              ...organizationEmail,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganizationEmailToCollectionIfMissing(organizationEmailCollection, organizationEmail);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganizationEmail to an array that doesn't contain it", () => {
          const organizationEmail: IOrganizationEmail = { id: 123 };
          const organizationEmailCollection: IOrganizationEmail[] = [{ id: 456 }];
          expectedResult = service.addOrganizationEmailToCollectionIfMissing(organizationEmailCollection, organizationEmail);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationEmail);
        });

        it('should add only unique OrganizationEmail to an array', () => {
          const organizationEmailArray: IOrganizationEmail[] = [{ id: 123 }, { id: 456 }, { id: 34311 }];
          const organizationEmailCollection: IOrganizationEmail[] = [{ id: 123 }];
          expectedResult = service.addOrganizationEmailToCollectionIfMissing(organizationEmailCollection, ...organizationEmailArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organizationEmail: IOrganizationEmail = { id: 123 };
          const organizationEmail2: IOrganizationEmail = { id: 456 };
          expectedResult = service.addOrganizationEmailToCollectionIfMissing([], organizationEmail, organizationEmail2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationEmail);
          expect(expectedResult).toContain(organizationEmail2);
        });

        it('should accept null and undefined values', () => {
          const organizationEmail: IOrganizationEmail = { id: 123 };
          expectedResult = service.addOrganizationEmailToCollectionIfMissing([], null, organizationEmail, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationEmail);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
