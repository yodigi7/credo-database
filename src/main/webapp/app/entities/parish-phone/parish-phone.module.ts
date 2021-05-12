import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ParishPhoneComponent } from './list/parish-phone.component';
import { ParishPhoneDetailComponent } from './detail/parish-phone-detail.component';
import { ParishPhoneUpdateComponent } from './update/parish-phone-update.component';
import { ParishPhoneDeleteDialogComponent } from './delete/parish-phone-delete-dialog.component';
import { ParishPhoneRoutingModule } from './route/parish-phone-routing.module';

@NgModule({
  imports: [SharedModule, ParishPhoneRoutingModule],
  declarations: [ParishPhoneComponent, ParishPhoneDetailComponent, ParishPhoneUpdateComponent, ParishPhoneDeleteDialogComponent],
  entryComponents: [ParishPhoneDeleteDialogComponent],
})
export class ParishPhoneModule {}
