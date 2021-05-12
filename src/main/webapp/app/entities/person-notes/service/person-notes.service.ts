import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonNotes, getPersonNotesIdentifier } from '../person-notes.model';

export type EntityResponseType = HttpResponse<IPersonNotes>;
export type EntityArrayResponseType = HttpResponse<IPersonNotes[]>;

@Injectable({ providedIn: 'root' })
export class PersonNotesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-notes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personNotes: IPersonNotes): Observable<EntityResponseType> {
    return this.http.post<IPersonNotes>(this.resourceUrl, personNotes, { observe: 'response' });
  }

  update(personNotes: IPersonNotes): Observable<EntityResponseType> {
    return this.http.put<IPersonNotes>(`${this.resourceUrl}/${getPersonNotesIdentifier(personNotes) as number}`, personNotes, {
      observe: 'response',
    });
  }

  partialUpdate(personNotes: IPersonNotes): Observable<EntityResponseType> {
    return this.http.patch<IPersonNotes>(`${this.resourceUrl}/${getPersonNotesIdentifier(personNotes) as number}`, personNotes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonNotes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonNotes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonNotesToCollectionIfMissing(
    personNotesCollection: IPersonNotes[],
    ...personNotesToCheck: (IPersonNotes | null | undefined)[]
  ): IPersonNotes[] {
    const personNotes: IPersonNotes[] = personNotesToCheck.filter(isPresent);
    if (personNotes.length > 0) {
      const personNotesCollectionIdentifiers = personNotesCollection.map(personNotesItem => getPersonNotesIdentifier(personNotesItem)!);
      const personNotesToAdd = personNotes.filter(personNotesItem => {
        const personNotesIdentifier = getPersonNotesIdentifier(personNotesItem);
        if (personNotesIdentifier == null || personNotesCollectionIdentifiers.includes(personNotesIdentifier)) {
          return false;
        }
        personNotesCollectionIdentifiers.push(personNotesIdentifier);
        return true;
      });
      return [...personNotesToAdd, ...personNotesCollection];
    }
    return personNotesCollection;
  }
}
