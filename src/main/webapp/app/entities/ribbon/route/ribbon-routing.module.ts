import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RibbonComponent } from '../list/ribbon.component';
import { RibbonDetailComponent } from '../detail/ribbon-detail.component';
import { RibbonUpdateComponent } from '../update/ribbon-update.component';
import { RibbonRoutingResolveService } from './ribbon-routing-resolve.service';

const ribbonRoute: Routes = [
  {
    path: '',
    component: RibbonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RibbonDetailComponent,
    resolve: {
      ribbon: RibbonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RibbonUpdateComponent,
    resolve: {
      ribbon: RibbonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RibbonUpdateComponent,
    resolve: {
      ribbon: RibbonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ribbonRoute)],
  exports: [RouterModule],
})
export class RibbonRoutingModule {}
