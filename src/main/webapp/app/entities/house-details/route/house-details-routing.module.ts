import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HouseDetailsComponent } from '../list/house-details.component';
import { HouseDetailsDetailComponent } from '../detail/house-details-detail.component';
import { HouseDetailsUpdateComponent } from '../update/house-details-update.component';
import { HouseDetailsRoutingResolveService } from './house-details-routing-resolve.service';

const houseDetailsRoute: Routes = [
  {
    path: '',
    component: HouseDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HouseDetailsDetailComponent,
    resolve: {
      houseDetails: HouseDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HouseDetailsUpdateComponent,
    resolve: {
      houseDetails: HouseDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HouseDetailsUpdateComponent,
    resolve: {
      houseDetails: HouseDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(houseDetailsRoute)],
  exports: [RouterModule],
})
export class HouseDetailsRoutingModule {}
