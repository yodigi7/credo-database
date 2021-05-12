import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ParishComponent } from './list/parish.component';
import { ParishDetailComponent } from './detail/parish-detail.component';
import { ParishUpdateComponent } from './update/parish-update.component';
import { ParishDeleteDialogComponent } from './delete/parish-delete-dialog.component';
import { ParishRoutingModule } from './route/parish-routing.module';

@NgModule({
  imports: [SharedModule, ParishRoutingModule],
  declarations: [ParishComponent, ParishDetailComponent, ParishUpdateComponent, ParishDeleteDialogComponent],
  entryComponents: [ParishDeleteDialogComponent],
})
export class ParishModule {}
