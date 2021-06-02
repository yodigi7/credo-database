import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ticketViewComponent } from './ticket-view.component';

export const TICKET_LOOKUP_ROUTE: Route = {
  path: 'ticket-view',
  component: ticketViewComponent,
  data: {
    authorities: [],
    pageTitle: 'ticket-view.title',
  },
  canActivate: [UserRouteAccessService],
};
