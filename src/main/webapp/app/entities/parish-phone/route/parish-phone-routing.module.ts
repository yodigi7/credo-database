import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParishPhoneComponent } from '../list/parish-phone.component';
import { ParishPhoneDetailComponent } from '../detail/parish-phone-detail.component';
import { ParishPhoneUpdateComponent } from '../update/parish-phone-update.component';
import { ParishPhoneRoutingResolveService } from './parish-phone-routing-resolve.service';

const parishPhoneRoute: Routes = [
  {
    path: '',
    component: ParishPhoneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParishPhoneDetailComponent,
    resolve: {
      parishPhone: ParishPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParishPhoneUpdateComponent,
    resolve: {
      parishPhone: ParishPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParishPhoneUpdateComponent,
    resolve: {
      parishPhone: ParishPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parishPhoneRoute)],
  exports: [RouterModule],
})
export class ParishPhoneRoutingModule {}
