import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HouseAddressComponent } from '../list/house-address.component';
import { HouseAddressDetailComponent } from '../detail/house-address-detail.component';
import { HouseAddressUpdateComponent } from '../update/house-address-update.component';
import { HouseAddressRoutingResolveService } from './house-address-routing-resolve.service';

const houseAddressRoute: Routes = [
  {
    path: '',
    component: HouseAddressComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HouseAddressDetailComponent,
    resolve: {
      houseAddress: HouseAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HouseAddressUpdateComponent,
    resolve: {
      houseAddress: HouseAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HouseAddressUpdateComponent,
    resolve: {
      houseAddress: HouseAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(houseAddressRoute)],
  exports: [RouterModule],
})
export class HouseAddressRoutingModule {}
