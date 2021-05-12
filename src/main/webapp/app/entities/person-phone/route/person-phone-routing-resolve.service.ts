import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonPhone, PersonPhone } from '../person-phone.model';
import { PersonPhoneService } from '../service/person-phone.service';

@Injectable({ providedIn: 'root' })
export class PersonPhoneRoutingResolveService implements Resolve<IPersonPhone> {
  constructor(protected service: PersonPhoneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonPhone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personPhone: HttpResponse<PersonPhone>) => {
          if (personPhone.body) {
            return of(personPhone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonPhone());
  }
}
