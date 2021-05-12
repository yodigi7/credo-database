import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonPhoneComponent } from '../list/person-phone.component';
import { PersonPhoneDetailComponent } from '../detail/person-phone-detail.component';
import { PersonPhoneUpdateComponent } from '../update/person-phone-update.component';
import { PersonPhoneRoutingResolveService } from './person-phone-routing-resolve.service';

const personPhoneRoute: Routes = [
  {
    path: '',
    component: PersonPhoneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonPhoneDetailComponent,
    resolve: {
      personPhone: PersonPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonPhoneUpdateComponent,
    resolve: {
      personPhone: PersonPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonPhoneUpdateComponent,
    resolve: {
      personPhone: PersonPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personPhoneRoute)],
  exports: [RouterModule],
})
export class PersonPhoneRoutingModule {}
