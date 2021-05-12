import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationPhoneComponent } from '../list/organization-phone.component';
import { OrganizationPhoneDetailComponent } from '../detail/organization-phone-detail.component';
import { OrganizationPhoneUpdateComponent } from '../update/organization-phone-update.component';
import { OrganizationPhoneRoutingResolveService } from './organization-phone-routing-resolve.service';

const organizationPhoneRoute: Routes = [
  {
    path: '',
    component: OrganizationPhoneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationPhoneDetailComponent,
    resolve: {
      organizationPhone: OrganizationPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationPhoneUpdateComponent,
    resolve: {
      organizationPhone: OrganizationPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationPhoneUpdateComponent,
    resolve: {
      organizationPhone: OrganizationPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organizationPhoneRoute)],
  exports: [RouterModule],
})
export class OrganizationPhoneRoutingModule {}
