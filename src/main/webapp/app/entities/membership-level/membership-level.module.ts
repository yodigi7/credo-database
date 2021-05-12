import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MembershipLevelComponent } from './list/membership-level.component';
import { MembershipLevelDetailComponent } from './detail/membership-level-detail.component';
import { MembershipLevelUpdateComponent } from './update/membership-level-update.component';
import { MembershipLevelDeleteDialogComponent } from './delete/membership-level-delete-dialog.component';
import { MembershipLevelRoutingModule } from './route/membership-level-routing.module';

@NgModule({
  imports: [SharedModule, MembershipLevelRoutingModule],
  declarations: [
    MembershipLevelComponent,
    MembershipLevelDetailComponent,
    MembershipLevelUpdateComponent,
    MembershipLevelDeleteDialogComponent,
  ],
  entryComponents: [MembershipLevelDeleteDialogComponent],
})
export class MembershipLevelModule {}
