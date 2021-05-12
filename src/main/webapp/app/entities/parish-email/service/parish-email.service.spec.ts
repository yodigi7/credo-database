import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { IParishEmail, ParishEmail } from '../parish-email.model';

import { ParishEmailService } from './parish-email.service';

describe('Service Tests', () => {
  describe('ParishEmail Service', () => {
    let service: ParishEmailService;
    let httpMock: HttpTestingController;
    let elemDefault: IParishEmail;
    let expectedResult: IParishEmail | IParishEmail[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ParishEmailService);
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

      it('should create a ParishEmail', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ParishEmail()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ParishEmail', () => {
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

      it('should partial update a ParishEmail', () => {
        const patchObject = Object.assign(
          {
            email: 'BBBBBB',
            type: 'BBBBBB',
            emailNewsletterSubscription: 'BBBBBB',
            emailEventNotificationSubscription: 'BBBBBB',
          },
          new ParishEmail()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ParishEmail', () => {
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

      it('should delete a ParishEmail', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addParishEmailToCollectionIfMissing', () => {
        it('should add a ParishEmail to an empty array', () => {
          const parishEmail: IParishEmail = { id: 123 };
          expectedResult = service.addParishEmailToCollectionIfMissing([], parishEmail);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parishEmail);
        });

        it('should not add a ParishEmail to an array that contains it', () => {
          const parishEmail: IParishEmail = { id: 123 };
          const parishEmailCollection: IParishEmail[] = [
            {
              ...parishEmail,
            },
            { id: 456 },
          ];
          expectedResult = service.addParishEmailToCollectionIfMissing(parishEmailCollection, parishEmail);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ParishEmail to an array that doesn't contain it", () => {
          const parishEmail: IParishEmail = { id: 123 };
          const parishEmailCollection: IParishEmail[] = [{ id: 456 }];
          expectedResult = service.addParishEmailToCollectionIfMissing(parishEmailCollection, parishEmail);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parishEmail);
        });

        it('should add only unique ParishEmail to an array', () => {
          const parishEmailArray: IParishEmail[] = [{ id: 123 }, { id: 456 }, { id: 56240 }];
          const parishEmailCollection: IParishEmail[] = [{ id: 123 }];
          expectedResult = service.addParishEmailToCollectionIfMissing(parishEmailCollection, ...parishEmailArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const parishEmail: IParishEmail = { id: 123 };
          const parishEmail2: IParishEmail = { id: 456 };
          expectedResult = service.addParishEmailToCollectionIfMissing([], parishEmail, parishEmail2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parishEmail);
          expect(expectedResult).toContain(parishEmail2);
        });

        it('should accept null and undefined values', () => {
          const parishEmail: IParishEmail = { id: 123 };
          expectedResult = service.addParishEmailToCollectionIfMissing([], null, parishEmail, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parishEmail);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
