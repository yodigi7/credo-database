import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonRoutingResolveService } from 'app/entities/person/route/person-routing-resolve.service';
import { ticketViewComponent } from './ticket-view.component';

export const TICKET_LOOKUP_ROUTE: Route = {
  path: 'person/:id/ticket-view',
  component: ticketViewComponent,
  resolve: {
    person: PersonRoutingResolveService,
  },
  canActivate: [UserRouteAccessService],
};
