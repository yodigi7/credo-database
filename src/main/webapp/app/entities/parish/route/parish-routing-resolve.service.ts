import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParish, Parish } from '../parish.model';
import { ParishService } from '../service/parish.service';

@Injectable({ providedIn: 'root' })
export class ParishRoutingResolveService implements Resolve<IParish> {
  constructor(protected service: ParishService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParish> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parish: HttpResponse<Parish>) => {
          if (parish.body) {
            return of(parish.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Parish());
  }
}
