import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INameTag, NameTag } from '../name-tag.model';
import { NameTagService } from '../service/name-tag.service';

@Injectable({ providedIn: 'root' })
export class NameTagRoutingResolveService implements Resolve<INameTag> {
  constructor(protected service: NameTagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INameTag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nameTag: HttpResponse<NameTag>) => {
          if (nameTag.body) {
            return of(nameTag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NameTag());
  }
}
