import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HohRoutingResolveService } from 'app/entities/person/route/hoh-routing-resolve.service';
import { EditPersonComponent } from './edit-person.component';

export const editPersonRoutes: Routes = [
  {
    path: 'edit-person',
    component: EditPersonComponent,
    data: {
      authorities: [],
      pageTitle: 'edit-person.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'edit-person/:id',
    component: EditPersonComponent,
    resolve: {
      person: HohRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];
