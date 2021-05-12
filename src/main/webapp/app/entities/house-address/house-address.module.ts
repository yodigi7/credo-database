import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { HouseAddressComponent } from './list/house-address.component';
import { HouseAddressDetailComponent } from './detail/house-address-detail.component';
import { HouseAddressUpdateComponent } from './update/house-address-update.component';
import { HouseAddressDeleteDialogComponent } from './delete/house-address-delete-dialog.component';
import { HouseAddressRoutingModule } from './route/house-address-routing.module';

@NgModule({
  imports: [SharedModule, HouseAddressRoutingModule],
  declarations: [HouseAddressComponent, HouseAddressDetailComponent, HouseAddressUpdateComponent, HouseAddressDeleteDialogComponent],
  entryComponents: [HouseAddressDeleteDialogComponent],
})
export class HouseAddressModule {}
