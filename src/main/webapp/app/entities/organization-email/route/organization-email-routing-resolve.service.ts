import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationEmail, OrganizationEmail } from '../organization-email.model';
import { OrganizationEmailService } from '../service/organization-email.service';

@Injectable({ providedIn: 'root' })
export class OrganizationEmailRoutingResolveService implements Resolve<IOrganizationEmail> {
  constructor(protected service: OrganizationEmailService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizationEmail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organizationEmail: HttpResponse<OrganizationEmail>) => {
          if (organizationEmail.body) {
            return of(organizationEmail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrganizationEmail());
  }
}
