import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INameTag, getNameTagIdentifier } from '../name-tag.model';

export type EntityResponseType = HttpResponse<INameTag>;
export type EntityArrayResponseType = HttpResponse<INameTag[]>;

@Injectable({ providedIn: 'root' })
export class NameTagService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/name-tags');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(nameTag: INameTag): Observable<EntityResponseType> {
    return this.http.post<INameTag>(this.resourceUrl, nameTag, { observe: 'response' });
  }

  update(nameTag: INameTag): Observable<EntityResponseType> {
    return this.http.put<INameTag>(`${this.resourceUrl}/${getNameTagIdentifier(nameTag) as number}`, nameTag, { observe: 'response' });
  }

  partialUpdate(nameTag: INameTag): Observable<EntityResponseType> {
    return this.http.patch<INameTag>(`${this.resourceUrl}/${getNameTagIdentifier(nameTag) as number}`, nameTag, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INameTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INameTag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNameTagToCollectionIfMissing(nameTagCollection: INameTag[], ...nameTagsToCheck: (INameTag | null | undefined)[]): INameTag[] {
    const nameTags: INameTag[] = nameTagsToCheck.filter(isPresent);
    if (nameTags.length > 0) {
      const nameTagCollectionIdentifiers = nameTagCollection.map(nameTagItem => getNameTagIdentifier(nameTagItem)!);
      const nameTagsToAdd = nameTags.filter(nameTagItem => {
        const nameTagIdentifier = getNameTagIdentifier(nameTagItem);
        if (nameTagIdentifier == null || nameTagCollectionIdentifiers.includes(nameTagIdentifier)) {
          return false;
        }
        nameTagCollectionIdentifiers.push(nameTagIdentifier);
        return true;
      });
      return [...nameTagsToAdd, ...nameTagCollection];
    }
    return nameTagCollection;
  }
}
