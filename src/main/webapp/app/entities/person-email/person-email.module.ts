import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonEmailComponent } from './list/person-email.component';
import { PersonEmailDetailComponent } from './detail/person-email-detail.component';
import { PersonEmailUpdateComponent } from './update/person-email-update.component';
import { PersonEmailDeleteDialogComponent } from './delete/person-email-delete-dialog.component';
import { PersonEmailRoutingModule } from './route/person-email-routing.module';

@NgModule({
  imports: [SharedModule, PersonEmailRoutingModule],
  declarations: [PersonEmailComponent, PersonEmailDetailComponent, PersonEmailUpdateComponent, PersonEmailDeleteDialogComponent],
  entryComponents: [PersonEmailDeleteDialogComponent],
})
export class PersonEmailModule {}
