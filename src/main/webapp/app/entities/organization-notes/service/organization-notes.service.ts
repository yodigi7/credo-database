import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationNotes, getOrganizationNotesIdentifier } from '../organization-notes.model';

export type EntityResponseType = HttpResponse<IOrganizationNotes>;
export type EntityArrayResponseType = HttpResponse<IOrganizationNotes[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationNotesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-notes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(organizationNotes: IOrganizationNotes): Observable<EntityResponseType> {
    return this.http.post<IOrganizationNotes>(this.resourceUrl, organizationNotes, { observe: 'response' });
  }

  update(organizationNotes: IOrganizationNotes): Observable<EntityResponseType> {
    return this.http.put<IOrganizationNotes>(
      `${this.resourceUrl}/${getOrganizationNotesIdentifier(organizationNotes) as number}`,
      organizationNotes,
      { observe: 'response' }
    );
  }

  partialUpdate(organizationNotes: IOrganizationNotes): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationNotes>(
      `${this.resourceUrl}/${getOrganizationNotesIdentifier(organizationNotes) as number}`,
      organizationNotes,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationNotes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationNotes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganizationNotesToCollectionIfMissing(
    organizationNotesCollection: IOrganizationNotes[],
    ...organizationNotesToCheck: (IOrganizationNotes | null | undefined)[]
  ): IOrganizationNotes[] {
    const organizationNotes: IOrganizationNotes[] = organizationNotesToCheck.filter(isPresent);
    if (organizationNotes.length > 0) {
      const organizationNotesCollectionIdentifiers = organizationNotesCollection.map(
        organizationNotesItem => getOrganizationNotesIdentifier(organizationNotesItem)!
      );
      const organizationNotesToAdd = organizationNotes.filter(organizationNotesItem => {
        const organizationNotesIdentifier = getOrganizationNotesIdentifier(organizationNotesItem);
        if (organizationNotesIdentifier == null || organizationNotesCollectionIdentifiers.includes(organizationNotesIdentifier)) {
          return false;
        }
        organizationNotesCollectionIdentifiers.push(organizationNotesIdentifier);
        return true;
      });
      return [...organizationNotesToAdd, ...organizationNotesCollection];
    }
    return organizationNotesCollection;
  }
}
