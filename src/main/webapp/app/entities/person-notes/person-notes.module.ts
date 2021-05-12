import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonNotesComponent } from './list/person-notes.component';
import { PersonNotesDetailComponent } from './detail/person-notes-detail.component';
import { PersonNotesUpdateComponent } from './update/person-notes-update.component';
import { PersonNotesDeleteDialogComponent } from './delete/person-notes-delete-dialog.component';
import { PersonNotesRoutingModule } from './route/person-notes-routing.module';

@NgModule({
  imports: [SharedModule, PersonNotesRoutingModule],
  declarations: [PersonNotesComponent, PersonNotesDetailComponent, PersonNotesUpdateComponent, PersonNotesDeleteDialogComponent],
  entryComponents: [PersonNotesDeleteDialogComponent],
})
export class PersonNotesModule {}
