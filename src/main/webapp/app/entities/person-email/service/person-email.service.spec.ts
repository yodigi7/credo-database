import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';
import { IPersonEmail, PersonEmail } from '../person-email.model';

import { PersonEmailService } from './person-email.service';

describe('Service Tests', () => {
  describe('PersonEmail Service', () => {
    let service: PersonEmailService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonEmail;
    let expectedResult: IPersonEmail | IPersonEmail[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonEmailService);
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

      it('should create a PersonEmail', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PersonEmail()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonEmail', () => {
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

      it('should partial update a PersonEmail', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            emailNewsletterSubscription: 'BBBBBB',
          },
          new PersonEmail()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonEmail', () => {
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

      it('should delete a PersonEmail', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonEmailToCollectionIfMissing', () => {
        it('should add a PersonEmail to an empty array', () => {
          const personEmail: IPersonEmail = { id: 123 };
          expectedResult = service.addPersonEmailToCollectionIfMissing([], personEmail);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personEmail);
        });

        it('should not add a PersonEmail to an array that contains it', () => {
          const personEmail: IPersonEmail = { id: 123 };
          const personEmailCollection: IPersonEmail[] = [
            {
              ...personEmail,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonEmailToCollectionIfMissing(personEmailCollection, personEmail);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonEmail to an array that doesn't contain it", () => {
          const personEmail: IPersonEmail = { id: 123 };
          const personEmailCollection: IPersonEmail[] = [{ id: 456 }];
          expectedResult = service.addPersonEmailToCollectionIfMissing(personEmailCollection, personEmail);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personEmail);
        });

        it('should add only unique PersonEmail to an array', () => {
          const personEmailArray: IPersonEmail[] = [{ id: 123 }, { id: 456 }, { id: 4557 }];
          const personEmailCollection: IPersonEmail[] = [{ id: 123 }];
          expectedResult = service.addPersonEmailToCollectionIfMissing(personEmailCollection, ...personEmailArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personEmail: IPersonEmail = { id: 123 };
          const personEmail2: IPersonEmail = { id: 456 };
          expectedResult = service.addPersonEmailToCollectionIfMissing([], personEmail, personEmail2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personEmail);
          expect(expectedResult).toContain(personEmail2);
        });

        it('should accept null and undefined values', () => {
          const personEmail: IPersonEmail = { id: 123 };
          expectedResult = service.addPersonEmailToCollectionIfMissing([], null, personEmail, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personEmail);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
