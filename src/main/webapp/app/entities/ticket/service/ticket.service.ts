import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITicket, getTicketIdentifier } from '../ticket.model';

export type EntityResponseType = HttpResponse<ITicket>;
export type EntityArrayResponseType = HttpResponse<ITicket[]>;

@Injectable({ providedIn: 'root' })
export class TicketService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tickets');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ticket: ITicket): Observable<EntityResponseType> {
    return this.http.post<ITicket>(this.resourceUrl, ticket, { observe: 'response' });
  }

  update(ticket: ITicket): Observable<EntityResponseType> {
    return this.http.put<ITicket>(`${this.resourceUrl}/${getTicketIdentifier(ticket) as number}`, ticket, { observe: 'response' });
  }

  partialUpdate(ticket: ITicket): Observable<EntityResponseType> {
    return this.http.patch<ITicket>(`${this.resourceUrl}/${getTicketIdentifier(ticket) as number}`, ticket, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITicket>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicket[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTicketToCollectionIfMissing(ticketCollection: ITicket[], ...ticketsToCheck: (ITicket | null | undefined)[]): ITicket[] {
    const tickets: ITicket[] = ticketsToCheck.filter(isPresent);
    if (tickets.length > 0) {
      const ticketCollectionIdentifiers = ticketCollection.map(ticketItem => getTicketIdentifier(ticketItem)!);
      const ticketsToAdd = tickets.filter(ticketItem => {
        const ticketIdentifier = getTicketIdentifier(ticketItem);
        if (ticketIdentifier == null || ticketCollectionIdentifiers.includes(ticketIdentifier)) {
          return false;
        }
        ticketCollectionIdentifiers.push(ticketIdentifier);
        return true;
      });
      return [...ticketsToAdd, ...ticketCollection];
    }
    return ticketCollection;
  }
}
