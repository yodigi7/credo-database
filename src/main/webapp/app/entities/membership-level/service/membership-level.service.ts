import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMembershipLevel, getMembershipLevelIdentifier } from '../membership-level.model';

export type EntityResponseType = HttpResponse<IMembershipLevel>;
export type EntityArrayResponseType = HttpResponse<IMembershipLevel[]>;

@Injectable({ providedIn: 'root' })
export class MembershipLevelService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/membership-levels');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(membershipLevel: IMembershipLevel): Observable<EntityResponseType> {
    return this.http.post<IMembershipLevel>(this.resourceUrl, membershipLevel, { observe: 'response' });
  }

  update(membershipLevel: IMembershipLevel): Observable<EntityResponseType> {
    return this.http.put<IMembershipLevel>(
      `${this.resourceUrl}/${getMembershipLevelIdentifier(membershipLevel) as number}`,
      membershipLevel,
      { observe: 'response' }
    );
  }

  partialUpdate(membershipLevel: IMembershipLevel): Observable<EntityResponseType> {
    return this.http.patch<IMembershipLevel>(
      `${this.resourceUrl}/${getMembershipLevelIdentifier(membershipLevel) as number}`,
      membershipLevel,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMembershipLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMembershipLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMembershipLevelToCollectionIfMissing(
    membershipLevelCollection: IMembershipLevel[],
    ...membershipLevelsToCheck: (IMembershipLevel | null | undefined)[]
  ): IMembershipLevel[] {
    const membershipLevels: IMembershipLevel[] = membershipLevelsToCheck.filter(isPresent);
    if (membershipLevels.length > 0) {
      const membershipLevelCollectionIdentifiers = membershipLevelCollection.map(
        membershipLevelItem => getMembershipLevelIdentifier(membershipLevelItem)!
      );
      const membershipLevelsToAdd = membershipLevels.filter(membershipLevelItem => {
        const membershipLevelIdentifier = getMembershipLevelIdentifier(membershipLevelItem);
        if (membershipLevelIdentifier == null || membershipLevelCollectionIdentifiers.includes(membershipLevelIdentifier)) {
          return false;
        }
        membershipLevelCollectionIdentifiers.push(membershipLevelIdentifier);
        return true;
      });
      return [...membershipLevelsToAdd, ...membershipLevelCollection];
    }
    return membershipLevelCollection;
  }
}
