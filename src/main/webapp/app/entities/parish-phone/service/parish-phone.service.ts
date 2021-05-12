import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParishPhone, getParishPhoneIdentifier } from '../parish-phone.model';

export type EntityResponseType = HttpResponse<IParishPhone>;
export type EntityArrayResponseType = HttpResponse<IParishPhone[]>;

@Injectable({ providedIn: 'root' })
export class ParishPhoneService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/parish-phones');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(parishPhone: IParishPhone): Observable<EntityResponseType> {
    return this.http.post<IParishPhone>(this.resourceUrl, parishPhone, { observe: 'response' });
  }

  update(parishPhone: IParishPhone): Observable<EntityResponseType> {
    return this.http.put<IParishPhone>(`${this.resourceUrl}/${getParishPhoneIdentifier(parishPhone) as number}`, parishPhone, {
      observe: 'response',
    });
  }

  partialUpdate(parishPhone: IParishPhone): Observable<EntityResponseType> {
    return this.http.patch<IParishPhone>(`${this.resourceUrl}/${getParishPhoneIdentifier(parishPhone) as number}`, parishPhone, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParishPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParishPhone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParishPhoneToCollectionIfMissing(
    parishPhoneCollection: IParishPhone[],
    ...parishPhonesToCheck: (IParishPhone | null | undefined)[]
  ): IParishPhone[] {
    const parishPhones: IParishPhone[] = parishPhonesToCheck.filter(isPresent);
    if (parishPhones.length > 0) {
      const parishPhoneCollectionIdentifiers = parishPhoneCollection.map(parishPhoneItem => getParishPhoneIdentifier(parishPhoneItem)!);
      const parishPhonesToAdd = parishPhones.filter(parishPhoneItem => {
        const parishPhoneIdentifier = getParishPhoneIdentifier(parishPhoneItem);
        if (parishPhoneIdentifier == null || parishPhoneCollectionIdentifiers.includes(parishPhoneIdentifier)) {
          return false;
        }
        parishPhoneCollectionIdentifiers.push(parishPhoneIdentifier);
        return true;
      });
      return [...parishPhonesToAdd, ...parishPhoneCollection];
    }
    return parishPhoneCollection;
  }
}
