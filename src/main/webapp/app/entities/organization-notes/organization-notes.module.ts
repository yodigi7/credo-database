import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrganizationNotesComponent } from './list/organization-notes.component';
import { OrganizationNotesDetailComponent } from './detail/organization-notes-detail.component';
import { OrganizationNotesUpdateComponent } from './update/organization-notes-update.component';
import { OrganizationNotesDeleteDialogComponent } from './delete/organization-notes-delete-dialog.component';
import { OrganizationNotesRoutingModule } from './route/organization-notes-routing.module';

@NgModule({
  imports: [SharedModule, OrganizationNotesRoutingModule],
  declarations: [
    OrganizationNotesComponent,
    OrganizationNotesDetailComponent,
    OrganizationNotesUpdateComponent,
    OrganizationNotesDeleteDialogComponent,
  ],
  entryComponents: [OrganizationNotesDeleteDialogComponent],
})
export class OrganizationNotesModule {}
