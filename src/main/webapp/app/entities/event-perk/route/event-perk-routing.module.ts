import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventPerkComponent } from '../list/event-perk.component';
import { EventPerkDetailComponent } from '../detail/event-perk-detail.component';
import { EventPerkUpdateComponent } from '../update/event-perk-update.component';
import { EventPerkRoutingResolveService } from './event-perk-routing-resolve.service';

const eventPerkRoute: Routes = [
  {
    path: '',
    component: EventPerkComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventPerkDetailComponent,
    resolve: {
      eventPerk: EventPerkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventPerkUpdateComponent,
    resolve: {
      eventPerk: EventPerkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventPerkUpdateComponent,
    resolve: {
      eventPerk: EventPerkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventPerkRoute)],
  exports: [RouterModule],
})
export class EventPerkRoutingModule {}
