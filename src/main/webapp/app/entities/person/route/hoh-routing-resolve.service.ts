import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';

@Injectable({ providedIn: 'root' })
export class HohRoutingResolveService implements Resolve<IPerson> {
  constructor(protected service: PersonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerson> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((person: HttpResponse<Person>) => {
          // TODO finish this and test it
          if (person.body?.headOfHouse?.id) {
            return this.service.find(person.body.headOfHouse.id).pipe(map(res => res.body as IPerson));
          }
          if (person.body) {
            return of(person.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Person());
  }
}
