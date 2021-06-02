import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INameTag, NameTag } from '../name-tag.model';

import { NameTagService } from './name-tag.service';

describe('Service Tests', () => {
  describe('NameTag Service', () => {
    let service: NameTagService;
    let httpMock: HttpTestingController;
    let elemDefault: INameTag;
    let expectedResult: INameTag | INameTag[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NameTagService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nameTag: 'AAAAAAA',
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

      it('should create a NameTag', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new NameTag()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NameTag', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameTag: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a NameTag', () => {
        const patchObject = Object.assign(
          {
            nameTag: 'BBBBBB',
          },
          new NameTag()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of NameTag', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameTag: 'BBBBBB',
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

      it('should delete a NameTag', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNameTagToCollectionIfMissing', () => {
        it('should add a NameTag to an empty array', () => {
          const nameTag: INameTag = { id: 123 };
          expectedResult = service.addNameTagToCollectionIfMissing([], nameTag);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nameTag);
        });

        it('should not add a NameTag to an array that contains it', () => {
          const nameTag: INameTag = { id: 123 };
          const nameTagCollection: INameTag[] = [
            {
              ...nameTag,
            },
            { id: 456 },
          ];
          expectedResult = service.addNameTagToCollectionIfMissing(nameTagCollection, nameTag);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a NameTag to an array that doesn't contain it", () => {
          const nameTag: INameTag = { id: 123 };
          const nameTagCollection: INameTag[] = [{ id: 456 }];
          expectedResult = service.addNameTagToCollectionIfMissing(nameTagCollection, nameTag);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nameTag);
        });

        it('should add only unique NameTag to an array', () => {
          const nameTagArray: INameTag[] = [{ id: 123 }, { id: 456 }, { id: 15066 }];
          const nameTagCollection: INameTag[] = [{ id: 123 }];
          expectedResult = service.addNameTagToCollectionIfMissing(nameTagCollection, ...nameTagArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const nameTag: INameTag = { id: 123 };
          const nameTag2: INameTag = { id: 456 };
          expectedResult = service.addNameTagToCollectionIfMissing([], nameTag, nameTag2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nameTag);
          expect(expectedResult).toContain(nameTag2);
        });

        it('should accept null and undefined values', () => {
          const nameTag: INameTag = { id: 123 };
          expectedResult = service.addNameTagToCollectionIfMissing([], null, nameTag, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nameTag);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
