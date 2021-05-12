import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonEmailComponent } from '../list/person-email.component';
import { PersonEmailDetailComponent } from '../detail/person-email-detail.component';
import { PersonEmailUpdateComponent } from '../update/person-email-update.component';
import { PersonEmailRoutingResolveService } from './person-email-routing-resolve.service';

const personEmailRoute: Routes = [
  {
    path: '',
    component: PersonEmailComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonEmailDetailComponent,
    resolve: {
      personEmail: PersonEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonEmailUpdateComponent,
    resolve: {
      personEmail: PersonEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonEmailUpdateComponent,
    resolve: {
      personEmail: PersonEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personEmailRoute)],
  exports: [RouterModule],
})
export class PersonEmailRoutingModule {}
