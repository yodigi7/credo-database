import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationAddressComponent } from '../list/organization-address.component';
import { OrganizationAddressDetailComponent } from '../detail/organization-address-detail.component';
import { OrganizationAddressUpdateComponent } from '../update/organization-address-update.component';
import { OrganizationAddressRoutingResolveService } from './organization-address-routing-resolve.service';

const organizationAddressRoute: Routes = [
  {
    path: '',
    component: OrganizationAddressComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationAddressDetailComponent,
    resolve: {
      organizationAddress: OrganizationAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationAddressUpdateComponent,
    resolve: {
      organizationAddress: OrganizationAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationAddressUpdateComponent,
    resolve: {
      organizationAddress: OrganizationAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organizationAddressRoute)],
  exports: [RouterModule],
})
export class OrganizationAddressRoutingModule {}
