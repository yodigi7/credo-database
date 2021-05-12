import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonEmail, PersonEmail } from '../person-email.model';
import { PersonEmailService } from '../service/person-email.service';

@Injectable({ providedIn: 'root' })
export class PersonEmailRoutingResolveService implements Resolve<IPersonEmail> {
  constructor(protected service: PersonEmailService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonEmail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personEmail: HttpResponse<PersonEmail>) => {
          if (personEmail.body) {
            return of(personEmail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonEmail());
  }
}
