import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParishEmailComponent } from '../list/parish-email.component';
import { ParishEmailDetailComponent } from '../detail/parish-email-detail.component';
import { ParishEmailUpdateComponent } from '../update/parish-email-update.component';
import { ParishEmailRoutingResolveService } from './parish-email-routing-resolve.service';

const parishEmailRoute: Routes = [
  {
    path: '',
    component: ParishEmailComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParishEmailDetailComponent,
    resolve: {
      parishEmail: ParishEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParishEmailUpdateComponent,
    resolve: {
      parishEmail: ParishEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParishEmailUpdateComponent,
    resolve: {
      parishEmail: ParishEmailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parishEmailRoute)],
  exports: [RouterModule],
})
export class ParishEmailRoutingModule {}
