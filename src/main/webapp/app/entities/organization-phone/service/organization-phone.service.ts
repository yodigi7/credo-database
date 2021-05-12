import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationPhone, getOrganizationPhoneIdentifier } from '../organization-phone.model';

export type EntityResponseType = HttpResponse<IOrganizationPhone>;
export type EntityArrayResponseType = HttpResponse<IOrganizationPhone[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationPhoneService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-phones');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(organizationPhone: IOrganizationPhone): Observable<EntityResponseType> {
    return this.http.post<IOrganizationPhone>(this.resourceUrl, organizationPhone, { observe: 'response' });
  }

  update(organizationPhone: IOrganizationPhone): Observable<EntityResponseType> {
    return this.http.put<IOrganizationPhone>(
      `${this.resourceUrl}/${getOrganizationPhoneIdentifier(organizationPhone) as number}`,
      organizationPhone,
      { observe: 'response' }
    );
  }

  partialUpdate(organizationPhone: IOrganizationPhone): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationPhone>(
      `${this.resourceUrl}/${getOrganizationPhoneIdentifier(organizationPhone) as number}`,
      organizationPhone,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationPhone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganizationPhoneToCollectionIfMissing(
    organizationPhoneCollection: IOrganizationPhone[],
    ...organizationPhonesToCheck: (IOrganizationPhone | null | undefined)[]
  ): IOrganizationPhone[] {
    const organizationPhones: IOrganizationPhone[] = organizationPhonesToCheck.filter(isPresent);
    if (organizationPhones.length > 0) {
      const organizationPhoneCollectionIdentifiers = organizationPhoneCollection.map(
        organizationPhoneItem => getOrganizationPhoneIdentifier(organizationPhoneItem)!
      );
      const organizationPhonesToAdd = organizationPhones.filter(organizationPhoneItem => {
        const organizationPhoneIdentifier = getOrganizationPhoneIdentifier(organizationPhoneItem);
        if (organizationPhoneIdentifier == null || organizationPhoneCollectionIdentifiers.includes(organizationPhoneIdentifier)) {
          return false;
        }
        organizationPhoneCollectionIdentifiers.push(organizationPhoneIdentifier);
        return true;
      });
      return [...organizationPhonesToAdd, ...organizationPhoneCollection];
    }
    return organizationPhoneCollection;
  }
}
