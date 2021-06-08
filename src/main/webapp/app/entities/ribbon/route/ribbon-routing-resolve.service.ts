import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRibbon, Ribbon } from '../ribbon.model';
import { RibbonService } from '../service/ribbon.service';

@Injectable({ providedIn: 'root' })
export class RibbonRoutingResolveService implements Resolve<IRibbon> {
  constructor(protected service: RibbonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRibbon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ribbon: HttpResponse<Ribbon>) => {
          if (ribbon.body) {
            return of(ribbon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ribbon());
  }
}
