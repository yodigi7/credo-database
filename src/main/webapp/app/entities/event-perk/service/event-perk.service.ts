import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventPerk, getEventPerkIdentifier } from '../event-perk.model';

export type EntityResponseType = HttpResponse<IEventPerk>;
export type EntityArrayResponseType = HttpResponse<IEventPerk[]>;

@Injectable({ providedIn: 'root' })
export class EventPerkService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/event-perks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(eventPerk: IEventPerk): Observable<EntityResponseType> {
    return this.http.post<IEventPerk>(this.resourceUrl, eventPerk, { observe: 'response' });
  }

  update(eventPerk: IEventPerk): Observable<EntityResponseType> {
    return this.http.put<IEventPerk>(`${this.resourceUrl}/${getEventPerkIdentifier(eventPerk) as number}`, eventPerk, {
      observe: 'response',
    });
  }

  partialUpdate(eventPerk: IEventPerk): Observable<EntityResponseType> {
    return this.http.patch<IEventPerk>(`${this.resourceUrl}/${getEventPerkIdentifier(eventPerk) as number}`, eventPerk, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventPerk>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventPerk[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventPerkToCollectionIfMissing(
    eventPerkCollection: IEventPerk[],
    ...eventPerksToCheck: (IEventPerk | null | undefined)[]
  ): IEventPerk[] {
    const eventPerks: IEventPerk[] = eventPerksToCheck.filter(isPresent);
    if (eventPerks.length > 0) {
      const eventPerkCollectionIdentifiers = eventPerkCollection.map(eventPerkItem => getEventPerkIdentifier(eventPerkItem)!);
      const eventPerksToAdd = eventPerks.filter(eventPerkItem => {
        const eventPerkIdentifier = getEventPerkIdentifier(eventPerkItem);
        if (eventPerkIdentifier == null || eventPerkCollectionIdentifiers.includes(eventPerkIdentifier)) {
          return false;
        }
        eventPerkCollectionIdentifiers.push(eventPerkIdentifier);
        return true;
      });
      return [...eventPerksToAdd, ...eventPerkCollection];
    }
    return eventPerkCollection;
  }
}
