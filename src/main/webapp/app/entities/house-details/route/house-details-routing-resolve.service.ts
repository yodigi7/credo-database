import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHouseDetails, HouseDetails } from '../house-details.model';
import { HouseDetailsService } from '../service/house-details.service';

@Injectable({ providedIn: 'root' })
export class HouseDetailsRoutingResolveService implements Resolve<IHouseDetails> {
  constructor(protected service: HouseDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHouseDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((houseDetails: HttpResponse<HouseDetails>) => {
          if (houseDetails.body) {
            return of(houseDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HouseDetails());
  }
}
