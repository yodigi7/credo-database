import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationEmail, getOrganizationEmailIdentifier } from '../organization-email.model';

export type EntityResponseType = HttpResponse<IOrganizationEmail>;
export type EntityArrayResponseType = HttpResponse<IOrganizationEmail[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationEmailService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-emails');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(organizationEmail: IOrganizationEmail): Observable<EntityResponseType> {
    return this.http.post<IOrganizationEmail>(this.resourceUrl, organizationEmail, { observe: 'response' });
  }

  update(organizationEmail: IOrganizationEmail): Observable<EntityResponseType> {
    return this.http.put<IOrganizationEmail>(
      `${this.resourceUrl}/${getOrganizationEmailIdentifier(organizationEmail) as number}`,
      organizationEmail,
      { observe: 'response' }
    );
  }

  partialUpdate(organizationEmail: IOrganizationEmail): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationEmail>(
      `${this.resourceUrl}/${getOrganizationEmailIdentifier(organizationEmail) as number}`,
      organizationEmail,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationEmail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationEmail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganizationEmailToCollectionIfMissing(
    organizationEmailCollection: IOrganizationEmail[],
    ...organizationEmailsToCheck: (IOrganizationEmail | null | undefined)[]
  ): IOrganizationEmail[] {
    const organizationEmails: IOrganizationEmail[] = organizationEmailsToCheck.filter(isPresent);
    if (organizationEmails.length > 0) {
      const organizationEmailCollectionIdentifiers = organizationEmailCollection.map(
        organizationEmailItem => getOrganizationEmailIdentifier(organizationEmailItem)!
      );
      const organizationEmailsToAdd = organizationEmails.filter(organizationEmailItem => {
        const organizationEmailIdentifier = getOrganizationEmailIdentifier(organizationEmailItem);
        if (organizationEmailIdentifier == null || organizationEmailCollectionIdentifiers.includes(organizationEmailIdentifier)) {
          return false;
        }
        organizationEmailCollectionIdentifiers.push(organizationEmailIdentifier);
        return true;
      });
      return [...organizationEmailsToAdd, ...organizationEmailCollection];
    }
    return organizationEmailCollection;
  }
}
