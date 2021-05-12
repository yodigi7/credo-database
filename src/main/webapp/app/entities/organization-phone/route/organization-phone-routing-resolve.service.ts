import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationPhone, OrganizationPhone } from '../organization-phone.model';
import { OrganizationPhoneService } from '../service/organization-phone.service';

@Injectable({ providedIn: 'root' })
export class OrganizationPhoneRoutingResolveService implements Resolve<IOrganizationPhone> {
  constructor(protected service: OrganizationPhoneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizationPhone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organizationPhone: HttpResponse<OrganizationPhone>) => {
          if (organizationPhone.body) {
            return of(organizationPhone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrganizationPhone());
  }
}
