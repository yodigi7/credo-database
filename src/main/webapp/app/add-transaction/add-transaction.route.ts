import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonRoutingResolveService } from 'app/entities/person/route/person-routing-resolve.service';
import { AddTransactionComponent } from './add-transaction.component';

export const ADD_TRANSACTION_ROUTE: Route = {
  path: 'person/:id/add-transaction',
  component: AddTransactionComponent,
  resolve: {
    person: PersonRoutingResolveService,
  },
  canActivate: [UserRouteAccessService],
};
