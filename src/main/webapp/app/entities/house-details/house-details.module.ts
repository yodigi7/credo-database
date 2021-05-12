import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { HouseDetailsComponent } from './list/house-details.component';
import { HouseDetailsDetailComponent } from './detail/house-details-detail.component';
import { HouseDetailsUpdateComponent } from './update/house-details-update.component';
import { HouseDetailsDeleteDialogComponent } from './delete/house-details-delete-dialog.component';
import { HouseDetailsRoutingModule } from './route/house-details-routing.module';

@NgModule({
  imports: [SharedModule, HouseDetailsRoutingModule],
  declarations: [HouseDetailsComponent, HouseDetailsDetailComponent, HouseDetailsUpdateComponent, HouseDetailsDeleteDialogComponent],
  entryComponents: [HouseDetailsDeleteDialogComponent],
})
export class HouseDetailsModule {}
