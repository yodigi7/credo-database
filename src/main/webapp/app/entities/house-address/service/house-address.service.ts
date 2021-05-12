import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHouseAddress, getHouseAddressIdentifier } from '../house-address.model';

export type EntityResponseType = HttpResponse<IHouseAddress>;
export type EntityArrayResponseType = HttpResponse<IHouseAddress[]>;

@Injectable({ providedIn: 'root' })
export class HouseAddressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/house-addresses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(houseAddress: IHouseAddress): Observable<EntityResponseType> {
    return this.http.post<IHouseAddress>(this.resourceUrl, houseAddress, { observe: 'response' });
  }

  update(houseAddress: IHouseAddress): Observable<EntityResponseType> {
    return this.http.put<IHouseAddress>(`${this.resourceUrl}/${getHouseAddressIdentifier(houseAddress) as number}`, houseAddress, {
      observe: 'response',
    });
  }

  partialUpdate(houseAddress: IHouseAddress): Observable<EntityResponseType> {
    return this.http.patch<IHouseAddress>(`${this.resourceUrl}/${getHouseAddressIdentifier(houseAddress) as number}`, houseAddress, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHouseAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHouseAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHouseAddressToCollectionIfMissing(
    houseAddressCollection: IHouseAddress[],
    ...houseAddressesToCheck: (IHouseAddress | null | undefined)[]
  ): IHouseAddress[] {
    const houseAddresses: IHouseAddress[] = houseAddressesToCheck.filter(isPresent);
    if (houseAddresses.length > 0) {
      const houseAddressCollectionIdentifiers = houseAddressCollection.map(
        houseAddressItem => getHouseAddressIdentifier(houseAddressItem)!
      );
      const houseAddressesToAdd = houseAddresses.filter(houseAddressItem => {
        const houseAddressIdentifier = getHouseAddressIdentifier(houseAddressItem);
        if (houseAddressIdentifier == null || houseAddressCollectionIdentifiers.includes(houseAddressIdentifier)) {
          return false;
        }
        houseAddressCollectionIdentifiers.push(houseAddressIdentifier);
        return true;
      });
      return [...houseAddressesToAdd, ...houseAddressCollection];
    }
    return houseAddressCollection;
  }
}
