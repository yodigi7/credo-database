import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrganizationPhoneComponent } from './list/organization-phone.component';
import { OrganizationPhoneDetailComponent } from './detail/organization-phone-detail.component';
import { OrganizationPhoneUpdateComponent } from './update/organization-phone-update.component';
import { OrganizationPhoneDeleteDialogComponent } from './delete/organization-phone-delete-dialog.component';
import { OrganizationPhoneRoutingModule } from './route/organization-phone-routing.module';

@NgModule({
  imports: [SharedModule, OrganizationPhoneRoutingModule],
  declarations: [
    OrganizationPhoneComponent,
    OrganizationPhoneDetailComponent,
    OrganizationPhoneUpdateComponent,
    OrganizationPhoneDeleteDialogComponent,
  ],
  entryComponents: [OrganizationPhoneDeleteDialogComponent],
})
export class OrganizationPhoneModule {}
