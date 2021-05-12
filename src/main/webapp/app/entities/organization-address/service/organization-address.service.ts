import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationAddress, getOrganizationAddressIdentifier } from '../organization-address.model';

export type EntityResponseType = HttpResponse<IOrganizationAddress>;
export type EntityArrayResponseType = HttpResponse<IOrganizationAddress[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationAddressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-addresses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(organizationAddress: IOrganizationAddress): Observable<EntityResponseType> {
    return this.http.post<IOrganizationAddress>(this.resourceUrl, organizationAddress, { observe: 'response' });
  }

  update(organizationAddress: IOrganizationAddress): Observable<EntityResponseType> {
    return this.http.put<IOrganizationAddress>(
      `${this.resourceUrl}/${getOrganizationAddressIdentifier(organizationAddress) as number}`,
      organizationAddress,
      { observe: 'response' }
    );
  }

  partialUpdate(organizationAddress: IOrganizationAddress): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationAddress>(
      `${this.resourceUrl}/${getOrganizationAddressIdentifier(organizationAddress) as number}`,
      organizationAddress,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrganizationAddressToCollectionIfMissing(
    organizationAddressCollection: IOrganizationAddress[],
    ...organizationAddressesToCheck: (IOrganizationAddress | null | undefined)[]
  ): IOrganizationAddress[] {
    const organizationAddresses: IOrganizationAddress[] = organizationAddressesToCheck.filter(isPresent);
    if (organizationAddresses.length > 0) {
      const organizationAddressCollectionIdentifiers = organizationAddressCollection.map(
        organizationAddressItem => getOrganizationAddressIdentifier(organizationAddressItem)!
      );
      const organizationAddressesToAdd = organizationAddresses.filter(organizationAddressItem => {
        const organizationAddressIdentifier = getOrganizationAddressIdentifier(organizationAddressItem);
        if (organizationAddressIdentifier == null || organizationAddressCollectionIdentifiers.includes(organizationAddressIdentifier)) {
          return false;
        }
        organizationAddressCollectionIdentifiers.push(organizationAddressIdentifier);
        return true;
      });
      return [...organizationAddressesToAdd, ...organizationAddressCollection];
    }
    return organizationAddressCollection;
  }
}
