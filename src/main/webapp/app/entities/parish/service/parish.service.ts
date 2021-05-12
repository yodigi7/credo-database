import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParish, getParishIdentifier } from '../parish.model';

export type EntityResponseType = HttpResponse<IParish>;
export type EntityArrayResponseType = HttpResponse<IParish[]>;

@Injectable({ providedIn: 'root' })
export class ParishService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/parishes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(parish: IParish): Observable<EntityResponseType> {
    return this.http.post<IParish>(this.resourceUrl, parish, { observe: 'response' });
  }

  update(parish: IParish): Observable<EntityResponseType> {
    return this.http.put<IParish>(`${this.resourceUrl}/${getParishIdentifier(parish) as number}`, parish, { observe: 'response' });
  }

  partialUpdate(parish: IParish): Observable<EntityResponseType> {
    return this.http.patch<IParish>(`${this.resourceUrl}/${getParishIdentifier(parish) as number}`, parish, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParish>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParish[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParishToCollectionIfMissing(parishCollection: IParish[], ...parishesToCheck: (IParish | null | undefined)[]): IParish[] {
    const parishes: IParish[] = parishesToCheck.filter(isPresent);
    if (parishes.length > 0) {
      const parishCollectionIdentifiers = parishCollection.map(parishItem => getParishIdentifier(parishItem)!);
      const parishesToAdd = parishes.filter(parishItem => {
        const parishIdentifier = getParishIdentifier(parishItem);
        if (parishIdentifier == null || parishCollectionIdentifiers.includes(parishIdentifier)) {
          return false;
        }
        parishCollectionIdentifiers.push(parishIdentifier);
        return true;
      });
      return [...parishesToAdd, ...parishCollection];
    }
    return parishCollection;
  }
}
