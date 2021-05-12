import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonNotes, PersonNotes } from '../person-notes.model';

import { PersonNotesService } from './person-notes.service';

describe('Service Tests', () => {
  describe('PersonNotes Service', () => {
    let service: PersonNotesService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonNotes;
    let expectedResult: IPersonNotes | IPersonNotes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonNotesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        notes: 'AAAAAAA',
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

      it('should create a PersonNotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PersonNotes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonNotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            notes: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PersonNotes', () => {
        const patchObject = Object.assign({}, new PersonNotes());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PersonNotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            notes: 'BBBBBB',
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

      it('should delete a PersonNotes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonNotesToCollectionIfMissing', () => {
        it('should add a PersonNotes to an empty array', () => {
          const personNotes: IPersonNotes = { id: 123 };
          expectedResult = service.addPersonNotesToCollectionIfMissing([], personNotes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personNotes);
        });

        it('should not add a PersonNotes to an array that contains it', () => {
          const personNotes: IPersonNotes = { id: 123 };
          const personNotesCollection: IPersonNotes[] = [
            {
              ...personNotes,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonNotesToCollectionIfMissing(personNotesCollection, personNotes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonNotes to an array that doesn't contain it", () => {
          const personNotes: IPersonNotes = { id: 123 };
          const personNotesCollection: IPersonNotes[] = [{ id: 456 }];
          expectedResult = service.addPersonNotesToCollectionIfMissing(personNotesCollection, personNotes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personNotes);
        });

        it('should add only unique PersonNotes to an array', () => {
          const personNotesArray: IPersonNotes[] = [{ id: 123 }, { id: 456 }, { id: 5365 }];
          const personNotesCollection: IPersonNotes[] = [{ id: 123 }];
          expectedResult = service.addPersonNotesToCollectionIfMissing(personNotesCollection, ...personNotesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personNotes: IPersonNotes = { id: 123 };
          const personNotes2: IPersonNotes = { id: 456 };
          expectedResult = service.addPersonNotesToCollectionIfMissing([], personNotes, personNotes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personNotes);
          expect(expectedResult).toContain(personNotes2);
        });

        it('should accept null and undefined values', () => {
          const personNotes: IPersonNotes = { id: 123 };
          expectedResult = service.addPersonNotesToCollectionIfMissing([], null, personNotes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personNotes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
