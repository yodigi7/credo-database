import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationNotesComponent } from '../list/organization-notes.component';
import { OrganizationNotesDetailComponent } from '../detail/organization-notes-detail.component';
import { OrganizationNotesUpdateComponent } from '../update/organization-notes-update.component';
import { OrganizationNotesRoutingResolveService } from './organization-notes-routing-resolve.service';

const organizationNotesRoute: Routes = [
  {
    path: '',
    component: OrganizationNotesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationNotesDetailComponent,
    resolve: {
      organizationNotes: OrganizationNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationNotesUpdateComponent,
    resolve: {
      organizationNotes: OrganizationNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationNotesUpdateComponent,
    resolve: {
      organizationNotes: OrganizationNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organizationNotesRoute)],
  exports: [RouterModule],
})
export class OrganizationNotesRoutingModule {}
