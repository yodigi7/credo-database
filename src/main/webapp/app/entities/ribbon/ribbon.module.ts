import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RibbonComponent } from './list/ribbon.component';
import { RibbonDetailComponent } from './detail/ribbon-detail.component';
import { RibbonUpdateComponent } from './update/ribbon-update.component';
import { RibbonDeleteDialogComponent } from './delete/ribbon-delete-dialog.component';
import { RibbonRoutingModule } from './route/ribbon-routing.module';

@NgModule({
  imports: [SharedModule, RibbonRoutingModule],
  declarations: [RibbonComponent, RibbonDetailComponent, RibbonUpdateComponent, RibbonDeleteDialogComponent],
  entryComponents: [RibbonDeleteDialogComponent],
})
export class RibbonModule {}
