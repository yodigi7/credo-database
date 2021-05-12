import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRelationship, Relationship } from '../relationship.model';
import { RelationshipService } from '../service/relationship.service';

@Injectable({ providedIn: 'root' })
export class RelationshipRoutingResolveService implements Resolve<IRelationship> {
  constructor(protected service: RelationshipService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRelationship> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((relationship: HttpResponse<Relationship>) => {
          if (relationship.body) {
            return of(relationship.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Relationship());
  }
}
