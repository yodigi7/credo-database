import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NameTagComponent } from '../list/name-tag.component';
import { NameTagDetailComponent } from '../detail/name-tag-detail.component';
import { NameTagUpdateComponent } from '../update/name-tag-update.component';
import { NameTagRoutingResolveService } from './name-tag-routing-resolve.service';

const nameTagRoute: Routes = [
  {
    path: '',
    component: NameTagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NameTagDetailComponent,
    resolve: {
      nameTag: NameTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NameTagUpdateComponent,
    resolve: {
      nameTag: NameTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NameTagUpdateComponent,
    resolve: {
      nameTag: NameTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nameTagRoute)],
  exports: [RouterModule],
})
export class NameTagRoutingModule {}
