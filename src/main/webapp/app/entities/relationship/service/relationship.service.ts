import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelationship, getRelationshipIdentifier } from '../relationship.model';

export type EntityResponseType = HttpResponse<IRelationship>;
export type EntityArrayResponseType = HttpResponse<IRelationship[]>;

@Injectable({ providedIn: 'root' })
export class RelationshipService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/relationships');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(relationship: IRelationship): Observable<EntityResponseType> {
    return this.http.post<IRelationship>(this.resourceUrl, relationship, { observe: 'response' });
  }

  update(relationship: IRelationship): Observable<EntityResponseType> {
    return this.http.put<IRelationship>(`${this.resourceUrl}/${getRelationshipIdentifier(relationship) as number}`, relationship, {
      observe: 'response',
    });
  }

  partialUpdate(relationship: IRelationship): Observable<EntityResponseType> {
    return this.http.patch<IRelationship>(`${this.resourceUrl}/${getRelationshipIdentifier(relationship) as number}`, relationship, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRelationship>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRelationship[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRelationshipToCollectionIfMissing(
    relationshipCollection: IRelationship[],
    ...relationshipsToCheck: (IRelationship | null | undefined)[]
  ): IRelationship[] {
    const relationships: IRelationship[] = relationshipsToCheck.filter(isPresent);
    if (relationships.length > 0) {
      const relationshipCollectionIdentifiers = relationshipCollection.map(
        relationshipItem => getRelationshipIdentifier(relationshipItem)!
      );
      const relationshipsToAdd = relationships.filter(relationshipItem => {
        const relationshipIdentifier = getRelationshipIdentifier(relationshipItem);
        if (relationshipIdentifier == null || relationshipCollectionIdentifiers.includes(relationshipIdentifier)) {
          return false;
        }
        relationshipCollectionIdentifiers.push(relationshipIdentifier);
        return true;
      });
      return [...relationshipsToAdd, ...relationshipCollection];
    }
    return relationshipCollection;
  }
}
