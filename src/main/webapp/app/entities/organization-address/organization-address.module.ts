import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrganizationAddressComponent } from './list/organization-address.component';
import { OrganizationAddressDetailComponent } from './detail/organization-address-detail.component';
import { OrganizationAddressUpdateComponent } from './update/organization-address-update.component';
import { OrganizationAddressDeleteDialogComponent } from './delete/organization-address-delete-dialog.component';
import { OrganizationAddressRoutingModule } from './route/organization-address-routing.module';

@NgModule({
  imports: [SharedModule, OrganizationAddressRoutingModule],
  declarations: [
    OrganizationAddressComponent,
    OrganizationAddressDetailComponent,
    OrganizationAddressUpdateComponent,
    OrganizationAddressDeleteDialogComponent,
  ],
  entryComponents: [OrganizationAddressDeleteDialogComponent],
})
export class OrganizationAddressModule {}
