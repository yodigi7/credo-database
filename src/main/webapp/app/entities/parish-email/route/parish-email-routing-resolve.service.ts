import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParishEmail, ParishEmail } from '../parish-email.model';
import { ParishEmailService } from '../service/parish-email.service';

@Injectable({ providedIn: 'root' })
export class ParishEmailRoutingResolveService implements Resolve<IParishEmail> {
  constructor(protected service: ParishEmailService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParishEmail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parishEmail: HttpResponse<ParishEmail>) => {
          if (parishEmail.body) {
            return of(parishEmail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ParishEmail());
  }
}
