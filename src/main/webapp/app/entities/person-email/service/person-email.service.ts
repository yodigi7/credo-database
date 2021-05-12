import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonEmail, getPersonEmailIdentifier } from '../person-email.model';

export type EntityResponseType = HttpResponse<IPersonEmail>;
export type EntityArrayResponseType = HttpResponse<IPersonEmail[]>;

@Injectable({ providedIn: 'root' })
export class PersonEmailService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/person-emails');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personEmail: IPersonEmail): Observable<EntityResponseType> {
    return this.http.post<IPersonEmail>(this.resourceUrl, personEmail, { observe: 'response' });
  }

  update(personEmail: IPersonEmail): Observable<EntityResponseType> {
    return this.http.put<IPersonEmail>(`${this.resourceUrl}/${getPersonEmailIdentifier(personEmail) as number}`, personEmail, {
      observe: 'response',
    });
  }

  partialUpdate(personEmail: IPersonEmail): Observable<EntityResponseType> {
    return this.http.patch<IPersonEmail>(`${this.resourceUrl}/${getPersonEmailIdentifier(personEmail) as number}`, personEmail, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonEmail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonEmail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonEmailToCollectionIfMissing(
    personEmailCollection: IPersonEmail[],
    ...personEmailsToCheck: (IPersonEmail | null | undefined)[]
  ): IPersonEmail[] {
    const personEmails: IPersonEmail[] = personEmailsToCheck.filter(isPresent);
    if (personEmails.length > 0) {
      const personEmailCollectionIdentifiers = personEmailCollection.map(personEmailItem => getPersonEmailIdentifier(personEmailItem)!);
      const personEmailsToAdd = personEmails.filter(personEmailItem => {
        const personEmailIdentifier = getPersonEmailIdentifier(personEmailItem);
        if (personEmailIdentifier == null || personEmailCollectionIdentifiers.includes(personEmailIdentifier)) {
          return false;
        }
        personEmailCollectionIdentifiers.push(personEmailIdentifier);
        return true;
      });
      return [...personEmailsToAdd, ...personEmailCollection];
    }
    return personEmailCollection;
  }
}
