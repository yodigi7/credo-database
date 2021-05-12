import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MembershipLevelComponent } from '../list/membership-level.component';
import { MembershipLevelDetailComponent } from '../detail/membership-level-detail.component';
import { MembershipLevelUpdateComponent } from '../update/membership-level-update.component';
import { MembershipLevelRoutingResolveService } from './membership-level-routing-resolve.service';

const membershipLevelRoute: Routes = [
  {
    path: '',
    component: MembershipLevelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MembershipLevelDetailComponent,
    resolve: {
      membershipLevel: MembershipLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MembershipLevelUpdateComponent,
    resolve: {
      membershipLevel: MembershipLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MembershipLevelUpdateComponent,
    resolve: {
      membershipLevel: MembershipLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(membershipLevelRoute)],
  exports: [RouterModule],
})
export class MembershipLevelRoutingModule {}
