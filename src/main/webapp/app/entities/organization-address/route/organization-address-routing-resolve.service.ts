import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationAddress, OrganizationAddress } from '../organization-address.model';
import { OrganizationAddressService } from '../service/organization-address.service';

@Injectable({ providedIn: 'root' })
export class OrganizationAddressRoutingResolveService implements Resolve<IOrganizationAddress> {
  constructor(protected service: OrganizationAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizationAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organizationAddress: HttpResponse<OrganizationAddress>) => {
          if (organizationAddress.body) {
            return of(organizationAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrganizationAddress());
  }
}
