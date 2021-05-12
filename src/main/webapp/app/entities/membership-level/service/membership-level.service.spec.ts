import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMembershipLevel, MembershipLevel } from '../membership-level.model';

import { MembershipLevelService } from './membership-level.service';

describe('Service Tests', () => {
  describe('MembershipLevel Service', () => {
    let service: MembershipLevelService;
    let httpMock: HttpTestingController;
    let elemDefault: IMembershipLevel;
    let expectedResult: IMembershipLevel | IMembershipLevel[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MembershipLevelService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        level: 'AAAAAAA',
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

      it('should create a MembershipLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MembershipLevel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MembershipLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            level: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MembershipLevel', () => {
        const patchObject = Object.assign(
          {
            level: 'BBBBBB',
          },
          new MembershipLevel()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MembershipLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            level: 'BBBBBB',
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

      it('should delete a MembershipLevel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMembershipLevelToCollectionIfMissing', () => {
        it('should add a MembershipLevel to an empty array', () => {
          const membershipLevel: IMembershipLevel = { id: 123 };
          expectedResult = service.addMembershipLevelToCollectionIfMissing([], membershipLevel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(membershipLevel);
        });

        it('should not add a MembershipLevel to an array that contains it', () => {
          const membershipLevel: IMembershipLevel = { id: 123 };
          const membershipLevelCollection: IMembershipLevel[] = [
            {
              ...membershipLevel,
            },
            { id: 456 },
          ];
          expectedResult = service.addMembershipLevelToCollectionIfMissing(membershipLevelCollection, membershipLevel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MembershipLevel to an array that doesn't contain it", () => {
          const membershipLevel: IMembershipLevel = { id: 123 };
          const membershipLevelCollection: IMembershipLevel[] = [{ id: 456 }];
          expectedResult = service.addMembershipLevelToCollectionIfMissing(membershipLevelCollection, membershipLevel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(membershipLevel);
        });

        it('should add only unique MembershipLevel to an array', () => {
          const membershipLevelArray: IMembershipLevel[] = [{ id: 123 }, { id: 456 }, { id: 56156 }];
          const membershipLevelCollection: IMembershipLevel[] = [{ id: 123 }];
          expectedResult = service.addMembershipLevelToCollectionIfMissing(membershipLevelCollection, ...membershipLevelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const membershipLevel: IMembershipLevel = { id: 123 };
          const membershipLevel2: IMembershipLevel = { id: 456 };
          expectedResult = service.addMembershipLevelToCollectionIfMissing([], membershipLevel, membershipLevel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(membershipLevel);
          expect(expectedResult).toContain(membershipLevel2);
        });

        it('should accept null and undefined values', () => {
          const membershipLevel: IMembershipLevel = { id: 123 };
          expectedResult = service.addMembershipLevelToCollectionIfMissing([], null, membershipLevel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(membershipLevel);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
