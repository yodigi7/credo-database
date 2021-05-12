import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHouseAddress, HouseAddress } from '../house-address.model';
import { HouseAddressService } from '../service/house-address.service';

@Injectable({ providedIn: 'root' })
export class HouseAddressRoutingResolveService implements Resolve<IHouseAddress> {
  constructor(protected service: HouseAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHouseAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((houseAddress: HttpResponse<HouseAddress>) => {
          if (houseAddress.body) {
            return of(houseAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HouseAddress());
  }
}
