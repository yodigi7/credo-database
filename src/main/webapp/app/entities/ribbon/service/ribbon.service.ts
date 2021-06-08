import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRibbon, getRibbonIdentifier } from '../ribbon.model';

export type EntityResponseType = HttpResponse<IRibbon>;
export type EntityArrayResponseType = HttpResponse<IRibbon[]>;

@Injectable({ providedIn: 'root' })
export class RibbonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ribbons');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ribbon: IRibbon): Observable<EntityResponseType> {
    return this.http.post<IRibbon>(this.resourceUrl, ribbon, { observe: 'response' });
  }

  update(ribbon: IRibbon): Observable<EntityResponseType> {
    return this.http.put<IRibbon>(`${this.resourceUrl}/${getRibbonIdentifier(ribbon) as number}`, ribbon, { observe: 'response' });
  }

  partialUpdate(ribbon: IRibbon): Observable<EntityResponseType> {
    return this.http.patch<IRibbon>(`${this.resourceUrl}/${getRibbonIdentifier(ribbon) as number}`, ribbon, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRibbon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRibbon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRibbonToCollectionIfMissing(ribbonCollection: IRibbon[], ...ribbonsToCheck: (IRibbon | null | undefined)[]): IRibbon[] {
    const ribbons: IRibbon[] = ribbonsToCheck.filter(isPresent);
    if (ribbons.length > 0) {
      const ribbonCollectionIdentifiers = ribbonCollection.map(ribbonItem => getRibbonIdentifier(ribbonItem)!);
      const ribbonsToAdd = ribbons.filter(ribbonItem => {
        const ribbonIdentifier = getRibbonIdentifier(ribbonItem);
        if (ribbonIdentifier == null || ribbonCollectionIdentifiers.includes(ribbonIdentifier)) {
          return false;
        }
        ribbonCollectionIdentifiers.push(ribbonIdentifier);
        return true;
      });
      return [...ribbonsToAdd, ...ribbonCollection];
    }
    return ribbonCollection;
  }
}
