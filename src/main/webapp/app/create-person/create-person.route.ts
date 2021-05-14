import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CreatePersonComponent } from './create-person.component';

export const CREATE_PERSON_ROUTE: Route = {
  path: 'create-person',
  component: CreatePersonComponent,
  data: {
    authorities: [],
    pageTitle: 'create-person.title',
  },
  canActivate: [UserRouteAccessService],
};
