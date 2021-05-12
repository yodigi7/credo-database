import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParishEmail, getParishEmailIdentifier } from '../parish-email.model';

export type EntityResponseType = HttpResponse<IParishEmail>;
export type EntityArrayResponseType = HttpResponse<IParishEmail[]>;

@Injectable({ providedIn: 'root' })
export class ParishEmailService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/parish-emails');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(parishEmail: IParishEmail): Observable<EntityResponseType> {
    return this.http.post<IParishEmail>(this.resourceUrl, parishEmail, { observe: 'response' });
  }

  update(parishEmail: IParishEmail): Observable<EntityResponseType> {
    return this.http.put<IParishEmail>(`${this.resourceUrl}/${getParishEmailIdentifier(parishEmail) as number}`, parishEmail, {
      observe: 'response',
    });
  }

  partialUpdate(parishEmail: IParishEmail): Observable<EntityResponseType> {
    return this.http.patch<IParishEmail>(`${this.resourceUrl}/${getParishEmailIdentifier(parishEmail) as number}`, parishEmail, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParishEmail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParishEmail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParishEmailToCollectionIfMissing(
    parishEmailCollection: IParishEmail[],
    ...parishEmailsToCheck: (IParishEmail | null | undefined)[]
  ): IParishEmail[] {
    const parishEmails: IParishEmail[] = parishEmailsToCheck.filter(isPresent);
    if (parishEmails.length > 0) {
      const parishEmailCollectionIdentifiers = parishEmailCollection.map(parishEmailItem => getParishEmailIdentifier(parishEmailItem)!);
      const parishEmailsToAdd = parishEmails.filter(parishEmailItem => {
        const parishEmailIdentifier = getParishEmailIdentifier(parishEmailItem);
        if (parishEmailIdentifier == null || parishEmailCollectionIdentifiers.includes(parishEmailIdentifier)) {
          return false;
        }
        parishEmailCollectionIdentifiers.push(parishEmailIdentifier);
        return true;
      });
      return [...parishEmailsToAdd, ...parishEmailCollection];
    }
    return parishEmailCollection;
  }
}
