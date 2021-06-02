import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NameTagComponent } from './list/name-tag.component';
import { NameTagDetailComponent } from './detail/name-tag-detail.component';
import { NameTagUpdateComponent } from './update/name-tag-update.component';
import { NameTagDeleteDialogComponent } from './delete/name-tag-delete-dialog.component';
import { NameTagRoutingModule } from './route/name-tag-routing.module';

@NgModule({
  imports: [SharedModule, NameTagRoutingModule],
  declarations: [NameTagComponent, NameTagDetailComponent, NameTagUpdateComponent, NameTagDeleteDialogComponent],
  entryComponents: [NameTagDeleteDialogComponent],
})
export class NameTagModule {}
