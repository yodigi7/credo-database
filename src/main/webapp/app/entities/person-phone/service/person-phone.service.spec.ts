import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonPhone, PersonPhone } from '../person-phone.model';

import { PersonPhoneService } from './person-phone.service';

describe('Service Tests', () => {
  describe('PersonPhone Service', () => {
    let service: PersonPhoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonPhone;
    let expectedResult: IPersonPhone | IPersonPhone[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonPhoneService);
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

      it('should create a PersonPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PersonPhone()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonPhone', () => {
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

      it('should partial update a PersonPhone', () => {
        const patchObject = Object.assign(
          {
            phoneNumber: 'BBBBBB',
          },
          new PersonPhone()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonPhone', () => {
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

      it('should delete a PersonPhone', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonPhoneToCollectionIfMissing', () => {
        it('should add a PersonPhone to an empty array', () => {
          const personPhone: IPersonPhone = { id: 123 };
          expectedResult = service.addPersonPhoneToCollectionIfMissing([], personPhone);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personPhone);
        });

        it('should not add a PersonPhone to an array that contains it', () => {
          const personPhone: IPersonPhone = { id: 123 };
          const personPhoneCollection: IPersonPhone[] = [
            {
              ...personPhone,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonPhoneToCollectionIfMissing(personPhoneCollection, personPhone);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonPhone to an array that doesn't contain it", () => {
          const personPhone: IPersonPhone = { id: 123 };
          const personPhoneCollection: IPersonPhone[] = [{ id: 456 }];
          expectedResult = service.addPersonPhoneToCollectionIfMissing(personPhoneCollection, personPhone);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personPhone);
        });

        it('should add only unique PersonPhone to an array', () => {
          const personPhoneArray: IPersonPhone[] = [{ id: 123 }, { id: 456 }, { id: 1753 }];
          const personPhoneCollection: IPersonPhone[] = [{ id: 123 }];
          expectedResult = service.addPersonPhoneToCollectionIfMissing(personPhoneCollection, ...personPhoneArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personPhone: IPersonPhone = { id: 123 };
          const personPhone2: IPersonPhone = { id: 456 };
          expectedResult = service.addPersonPhoneToCollectionIfMissing([], personPhone, personPhone2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personPhone);
          expect(expectedResult).toContain(personPhone2);
        });

        it('should accept null and undefined values', () => {
          const personPhone: IPersonPhone = { id: 123 };
          expectedResult = service.addPersonPhoneToCollectionIfMissing([], null, personPhone, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personPhone);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
