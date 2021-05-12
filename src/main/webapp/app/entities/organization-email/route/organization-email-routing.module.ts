import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationEmailComponent } from '../list/organization-email.component';
import { OrganizationEmailDetailComponent } from '../detail/organization-email-detail.component';
import { OrganizationEmailUpdateComponent } from '../update/organization-email-update.component';
import { OrganizationEmailRoutingResolveService } from './organization-email-routing-resolve.service';

const organizationEmailRoute: Routes = [
  {
    path: '',
    component: OrganizationEmailComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationEmailDetailComponent,
    resolve: {
      organizationEmail: OrganizationEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationEmailUpdateComponent,
    resolve: {
      organizationEmail: OrganizationEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationEmailUpdateComponent,
    resolve: {
      organizationEmail: OrganizationEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organizationEmailRoute)],
  exports: [RouterModule],
})
export class OrganizationEmailRoutingModule {}
