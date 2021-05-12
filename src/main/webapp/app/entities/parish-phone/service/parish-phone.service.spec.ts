import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParishPhone, ParishPhone } from '../parish-phone.model';

import { ParishPhoneService } from './parish-phone.service';

describe('Service Tests', () => {
  describe('ParishPhone Service', () => {
    let service: ParishPhoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IParishPhone;
    let expectedResult: IParishPhone | IParishPhone[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ParishPhoneService);
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

      it('should create a ParishPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ParishPhone()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ParishPhone', () => {
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

      it('should partial update a ParishPhone', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
          },
          new ParishPhone()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ParishPhone', () => {
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

      it('should delete a ParishPhone', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addParishPhoneToCollectionIfMissing', () => {
        it('should add a ParishPhone to an empty array', () => {
          const parishPhone: IParishPhone = { id: 123 };
          expectedResult = service.addParishPhoneToCollectionIfMissing([], parishPhone);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parishPhone);
        });

        it('should not add a ParishPhone to an array that contains it', () => {
          const parishPhone: IParishPhone = { id: 123 };
          const parishPhoneCollection: IParishPhone[] = [
            {
              ...parishPhone,
            },
            { id: 456 },
          ];
          expectedResult = service.addParishPhoneToCollectionIfMissing(parishPhoneCollection, parishPhone);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ParishPhone to an array that doesn't contain it", () => {
          const parishPhone: IParishPhone = { id: 123 };
          const parishPhoneCollection: IParishPhone[] = [{ id: 456 }];
          expectedResult = service.addParishPhoneToCollectionIfMissing(parishPhoneCollection, parishPhone);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parishPhone);
        });

        it('should add only unique ParishPhone to an array', () => {
          const parishPhoneArray: IParishPhone[] = [{ id: 123 }, { id: 456 }, { id: 53860 }];
          const parishPhoneCollection: IParishPhone[] = [{ id: 123 }];
          expectedResult = service.addParishPhoneToCollectionIfMissing(parishPhoneCollection, ...parishPhoneArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const parishPhone: IParishPhone = { id: 123 };
          const parishPhone2: IParishPhone = { id: 456 };
          expectedResult = service.addParishPhoneToCollectionIfMissing([], parishPhone, parishPhone2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parishPhone);
          expect(expectedResult).toContain(parishPhone2);
        });

        it('should accept null and undefined values', () => {
          const parishPhone: IParishPhone = { id: 123 };
          expectedResult = service.addParishPhoneToCollectionIfMissing([], null, parishPhone, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parishPhone);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
