import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EditPersonSubformComponent } from 'app/custom-forms/edit-person-subform/edit-person-subform.component';
import { PersonRoutingResolveService } from 'app/entities/person/route/person-routing-resolve.service';
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
      person: PersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];
