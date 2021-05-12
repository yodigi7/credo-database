import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonPhone, getPersonPhoneIdentifier } from '../person-phone.model';

export type EntityResponseType = HttpResponse<IPersonPhone>;
export type EntityArrayResponseType = HttpResponse<IPersonPhone[]>;

@Injectable({ providedIn: 'root' })
export class PersonPhoneService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-phones');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personPhone: IPersonPhone): Observable<EntityResponseType> {
    return this.http.post<IPersonPhone>(this.resourceUrl, personPhone, { observe: 'response' });
  }

  update(personPhone: IPersonPhone): Observable<EntityResponseType> {
    return this.http.put<IPersonPhone>(`${this.resourceUrl}/${getPersonPhoneIdentifier(personPhone) as number}`, personPhone, {
      observe: 'response',
    });
  }

  partialUpdate(personPhone: IPersonPhone): Observable<EntityResponseType> {
    return this.http.patch<IPersonPhone>(`${this.resourceUrl}/${getPersonPhoneIdentifier(personPhone) as number}`, personPhone, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonPhone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonPhoneToCollectionIfMissing(
    personPhoneCollection: IPersonPhone[],
    ...personPhonesToCheck: (IPersonPhone | null | undefined)[]
  ): IPersonPhone[] {
    const personPhones: IPersonPhone[] = personPhonesToCheck.filter(isPresent);
    if (personPhones.length > 0) {
      const personPhoneCollectionIdentifiers = personPhoneCollection.map(personPhoneItem => getPersonPhoneIdentifier(personPhoneItem)!);
      const personPhonesToAdd = personPhones.filter(personPhoneItem => {
        const personPhoneIdentifier = getPersonPhoneIdentifier(personPhoneItem);
        if (personPhoneIdentifier == null || personPhoneCollectionIdentifiers.includes(personPhoneIdentifier)) {
          return false;
        }
        personPhoneCollectionIdentifiers.push(personPhoneIdentifier);
        return true;
      });
      return [...personPhonesToAdd, ...personPhoneCollection];
    }
    return personPhoneCollection;
  }
}
