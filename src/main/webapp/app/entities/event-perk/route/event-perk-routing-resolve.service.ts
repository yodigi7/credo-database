import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventPerk, EventPerk } from '../event-perk.model';
import { EventPerkService } from '../service/event-perk.service';

@Injectable({ providedIn: 'root' })
export class EventPerkRoutingResolveService implements Resolve<IEventPerk> {
  constructor(protected service: EventPerkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventPerk> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventPerk: HttpResponse<EventPerk>) => {
          if (eventPerk.body) {
            return of(eventPerk.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventPerk());
  }
}
