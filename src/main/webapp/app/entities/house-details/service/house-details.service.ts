import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHouseDetails, getHouseDetailsIdentifier } from '../house-details.model';

export type EntityResponseType = HttpResponse<IHouseDetails>;
export type EntityArrayResponseType = HttpResponse<IHouseDetails[]>;

@Injectable({ providedIn: 'root' })
export class HouseDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/house-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(houseDetails: IHouseDetails): Observable<EntityResponseType> {
    return this.http.post<IHouseDetails>(this.resourceUrl, houseDetails, { observe: 'response' });
  }

  update(houseDetails: IHouseDetails): Observable<EntityResponseType> {
    return this.http.put<IHouseDetails>(`${this.resourceUrl}/${getHouseDetailsIdentifier(houseDetails) as number}`, houseDetails, {
      observe: 'response',
    });
  }

  partialUpdate(houseDetails: IHouseDetails): Observable<EntityResponseType> {
    return this.http.patch<IHouseDetails>(`${this.resourceUrl}/${getHouseDetailsIdentifier(houseDetails) as number}`, houseDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHouseDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHouseDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHouseDetailsToCollectionIfMissing(
    houseDetailsCollection: IHouseDetails[],
    ...houseDetailsToCheck: (IHouseDetails | null | undefined)[]
  ): IHouseDetails[] {
    const houseDetails: IHouseDetails[] = houseDetailsToCheck.filter(isPresent);
    if (houseDetails.length > 0) {
      const houseDetailsCollectionIdentifiers = houseDetailsCollection.map(
        houseDetailsItem => getHouseDetailsIdentifier(houseDetailsItem)!
      );
      const houseDetailsToAdd = houseDetails.filter(houseDetailsItem => {
        const houseDetailsIdentifier = getHouseDetailsIdentifier(houseDetailsItem);
        if (houseDetailsIdentifier == null || houseDetailsCollectionIdentifiers.includes(houseDetailsIdentifier)) {
          return false;
        }
        houseDetailsCollectionIdentifiers.push(houseDetailsIdentifier);
        return true;
      });
      return [...houseDetailsToAdd, ...houseDetailsCollection];
    }
    return houseDetailsCollection;
  }
}
