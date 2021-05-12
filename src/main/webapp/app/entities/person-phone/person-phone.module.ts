import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonPhoneComponent } from './list/person-phone.component';
import { PersonPhoneDetailComponent } from './detail/person-phone-detail.component';
import { PersonPhoneUpdateComponent } from './update/person-phone-update.component';
import { PersonPhoneDeleteDialogComponent } from './delete/person-phone-delete-dialog.component';
import { PersonPhoneRoutingModule } from './route/person-phone-routing.module';

@NgModule({
  imports: [SharedModule, PersonPhoneRoutingModule],
  declarations: [PersonPhoneComponent, PersonPhoneDetailComponent, PersonPhoneUpdateComponent, PersonPhoneDeleteDialogComponent],
  entryComponents: [PersonPhoneDeleteDialogComponent],
})
export class PersonPhoneModule {}
