import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY, merge } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';

@Injectable({ providedIn: 'root' })
export class PersonRoutingResolveService implements Resolve<IPerson> {
  constructor(protected service: PersonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerson> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((person: HttpResponse<Person>) => {
          // TODO finish this and test it
          // if (person.body?.headOfHouse?.id) {
          //   this.service.find(person.body.headOfHouse.id).pipe(
          //     mergeMap((innerPerson: HttpResponse<Person>) => {
          //       return of(innerPerson.body);
          //     })
          //   )
          // }
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
