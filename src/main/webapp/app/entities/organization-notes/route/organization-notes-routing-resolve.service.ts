import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationNotes, OrganizationNotes } from '../organization-notes.model';
import { OrganizationNotesService } from '../service/organization-notes.service';

@Injectable({ providedIn: 'root' })
export class OrganizationNotesRoutingResolveService implements Resolve<IOrganizationNotes> {
  constructor(protected service: OrganizationNotesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizationNotes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organizationNotes: HttpResponse<OrganizationNotes>) => {
          if (organizationNotes.body) {
            return of(organizationNotes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrganizationNotes());
  }
}
