import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonNotesComponent } from '../list/person-notes.component';
import { PersonNotesDetailComponent } from '../detail/person-notes-detail.component';
import { PersonNotesUpdateComponent } from '../update/person-notes-update.component';
import { PersonNotesRoutingResolveService } from './person-notes-routing-resolve.service';

const personNotesRoute: Routes = [
  {
    path: '',
    component: PersonNotesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonNotesDetailComponent,
    resolve: {
      personNotes: PersonNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonNotesUpdateComponent,
    resolve: {
      personNotes: PersonNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonNotesUpdateComponent,
    resolve: {
      personNotes: PersonNotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personNotesRoute)],
  exports: [RouterModule],
})
export class PersonNotesRoutingModule {}
