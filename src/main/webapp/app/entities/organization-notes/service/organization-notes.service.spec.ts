import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationNotes, OrganizationNotes } from '../organization-notes.model';

import { OrganizationNotesService } from './organization-notes.service';

describe('Service Tests', () => {
  describe('OrganizationNotes Service', () => {
    let service: OrganizationNotesService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganizationNotes;
    let expectedResult: IOrganizationNotes | IOrganizationNotes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganizationNotesService);
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

      it('should create a OrganizationNotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrganizationNotes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganizationNotes', () => {
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

      it('should partial update a OrganizationNotes', () => {
        const patchObject = Object.assign(
          {
            notes: 'BBBBBB',
          },
          new OrganizationNotes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrganizationNotes', () => {
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

      it('should delete a OrganizationNotes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganizationNotesToCollectionIfMissing', () => {
        it('should add a OrganizationNotes to an empty array', () => {
          const organizationNotes: IOrganizationNotes = { id: 123 };
          expectedResult = service.addOrganizationNotesToCollectionIfMissing([], organizationNotes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationNotes);
        });

        it('should not add a OrganizationNotes to an array that contains it', () => {
          const organizationNotes: IOrganizationNotes = { id: 123 };
          const organizationNotesCollection: IOrganizationNotes[] = [
            {
              ...organizationNotes,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganizationNotesToCollectionIfMissing(organizationNotesCollection, organizationNotes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganizationNotes to an array that doesn't contain it", () => {
          const organizationNotes: IOrganizationNotes = { id: 123 };
          const organizationNotesCollection: IOrganizationNotes[] = [{ id: 456 }];
          expectedResult = service.addOrganizationNotesToCollectionIfMissing(organizationNotesCollection, organizationNotes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationNotes);
        });

        it('should add only unique OrganizationNotes to an array', () => {
          const organizationNotesArray: IOrganizationNotes[] = [{ id: 123 }, { id: 456 }, { id: 22370 }];
          const organizationNotesCollection: IOrganizationNotes[] = [{ id: 123 }];
          expectedResult = service.addOrganizationNotesToCollectionIfMissing(organizationNotesCollection, ...organizationNotesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organizationNotes: IOrganizationNotes = { id: 123 };
          const organizationNotes2: IOrganizationNotes = { id: 456 };
          expectedResult = service.addOrganizationNotesToCollectionIfMissing([], organizationNotes, organizationNotes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organizationNotes);
          expect(expectedResult).toContain(organizationNotes2);
        });

        it('should accept null and undefined values', () => {
          const organizationNotes: IOrganizationNotes = { id: 123 };
          expectedResult = service.addOrganizationNotesToCollectionIfMissing([], null, organizationNotes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organizationNotes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
