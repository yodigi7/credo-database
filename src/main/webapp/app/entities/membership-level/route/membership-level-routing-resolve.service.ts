import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMembershipLevel, MembershipLevel } from '../membership-level.model';
import { MembershipLevelService } from '../service/membership-level.service';

@Injectable({ providedIn: 'root' })
export class MembershipLevelRoutingResolveService implements Resolve<IMembershipLevel> {
  constructor(protected service: MembershipLevelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMembershipLevel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((membershipLevel: HttpResponse<MembershipLevel>) => {
          if (membershipLevel.body) {
            return of(membershipLevel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MembershipLevel());
  }
}
