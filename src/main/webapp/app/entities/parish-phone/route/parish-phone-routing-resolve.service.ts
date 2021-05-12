import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParishPhone, ParishPhone } from '../parish-phone.model';
import { ParishPhoneService } from '../service/parish-phone.service';

@Injectable({ providedIn: 'root' })
export class ParishPhoneRoutingResolveService implements Resolve<IParishPhone> {
  constructor(protected service: ParishPhoneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParishPhone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parishPhone: HttpResponse<ParishPhone>) => {
          if (parishPhone.body) {
            return of(parishPhone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ParishPhone());
  }
}
