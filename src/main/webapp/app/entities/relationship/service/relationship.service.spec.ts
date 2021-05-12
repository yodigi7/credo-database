import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRelationship, Relationship } from '../relationship.model';

import { RelationshipService } from './relationship.service';

describe('Service Tests', () => {
  describe('Relationship Service', () => {
    let service: RelationshipService;
    let httpMock: HttpTestingController;
    let elemDefault: IRelationship;
    let expectedResult: IRelationship | IRelationship[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RelationshipService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        relationship: 'AAAAAAA',
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

      it('should create a Relationship', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Relationship()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Relationship', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            relationship: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Relationship', () => {
        const patchObject = Object.assign(
          {
            relationship: 'BBBBBB',
          },
          new Relationship()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Relationship', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            relationship: 'BBBBBB',
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

      it('should delete a Relationship', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRelationshipToCollectionIfMissing', () => {
        it('should add a Relationship to an empty array', () => {
          const relationship: IRelationship = { id: 123 };
          expectedResult = service.addRelationshipToCollectionIfMissing([], relationship);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relationship);
        });

        it('should not add a Relationship to an array that contains it', () => {
          const relationship: IRelationship = { id: 123 };
          const relationshipCollection: IRelationship[] = [
            {
              ...relationship,
            },
            { id: 456 },
          ];
          expectedResult = service.addRelationshipToCollectionIfMissing(relationshipCollection, relationship);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Relationship to an array that doesn't contain it", () => {
          const relationship: IRelationship = { id: 123 };
          const relationshipCollection: IRelationship[] = [{ id: 456 }];
          expectedResult = service.addRelationshipToCollectionIfMissing(relationshipCollection, relationship);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relationship);
        });

        it('should add only unique Relationship to an array', () => {
          const relationshipArray: IRelationship[] = [{ id: 123 }, { id: 456 }, { id: 98769 }];
          const relationshipCollection: IRelationship[] = [{ id: 123 }];
          expectedResult = service.addRelationshipToCollectionIfMissing(relationshipCollection, ...relationshipArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const relationship: IRelationship = { id: 123 };
          const relationship2: IRelationship = { id: 456 };
          expectedResult = service.addRelationshipToCollectionIfMissing([], relationship, relationship2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relationship);
          expect(expectedResult).toContain(relationship2);
        });

        it('should accept null and undefined values', () => {
          const relationship: IRelationship = { id: 123 };
          expectedResult = service.addRelationshipToCollectionIfMissing([], null, relationship, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relationship);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
