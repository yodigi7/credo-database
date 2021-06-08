import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EventPerkComponent } from './list/event-perk.component';
import { EventPerkDetailComponent } from './detail/event-perk-detail.component';
import { EventPerkUpdateComponent } from './update/event-perk-update.component';
import { EventPerkDeleteDialogComponent } from './delete/event-perk-delete-dialog.component';
import { EventPerkRoutingModule } from './route/event-perk-routing.module';

@NgModule({
  imports: [SharedModule, EventPerkRoutingModule],
  declarations: [EventPerkComponent, EventPerkDetailComponent, EventPerkUpdateComponent, EventPerkDeleteDialogComponent],
  entryComponents: [EventPerkDeleteDialogComponent],
})
export class EventPerkModule {}
