import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonNotes, PersonNotes } from '../person-notes.model';
import { PersonNotesService } from '../service/person-notes.service';

@Injectable({ providedIn: 'root' })
export class PersonNotesRoutingResolveService implements Resolve<IPersonNotes> {
  constructor(protected service: PersonNotesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonNotes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personNotes: HttpResponse<PersonNotes>) => {
          if (personNotes.body) {
            return of(personNotes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonNotes());
  }
}
