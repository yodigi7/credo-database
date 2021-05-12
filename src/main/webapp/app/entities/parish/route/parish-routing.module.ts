import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParishComponent } from '../list/parish.component';
import { ParishDetailComponent } from '../detail/parish-detail.component';
import { ParishUpdateComponent } from '../update/parish-update.component';
import { ParishRoutingResolveService } from './parish-routing-resolve.service';

const parishRoute: Routes = [
  {
    path: '',
    component: ParishComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParishDetailComponent,
    resolve: {
      parish: ParishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParishUpdateComponent,
    resolve: {
      parish: ParishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParishUpdateComponent,
    resolve: {
      parish: ParishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parishRoute)],
  exports: [RouterModule],
})
export class ParishRoutingModule {}
