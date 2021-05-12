import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ParishEmailComponent } from './list/parish-email.component';
import { ParishEmailDetailComponent } from './detail/parish-email-detail.component';
import { ParishEmailUpdateComponent } from './update/parish-email-update.component';
import { ParishEmailDeleteDialogComponent } from './delete/parish-email-delete-dialog.component';
import { ParishEmailRoutingModule } from './route/parish-email-routing.module';

@NgModule({
  imports: [SharedModule, ParishEmailRoutingModule],
  declarations: [ParishEmailComponent, ParishEmailDetailComponent, ParishEmailUpdateComponent, ParishEmailDeleteDialogComponent],
  entryComponents: [ParishEmailDeleteDialogComponent],
})
export class ParishEmailModule {}
