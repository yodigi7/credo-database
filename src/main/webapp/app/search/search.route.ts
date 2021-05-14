import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SearchComponent } from './search.component';

export const SEARCH_ROUTE: Route = {
  path: 'search',
  component: SearchComponent,
  data: {
    authorities: [],
    pageTitle: 'search.title',
  },
  canActivate: [UserRouteAccessService],
};
