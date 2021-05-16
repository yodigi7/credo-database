import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CoreEntitiesModule } from '../entities/core-entities.module';
import { EditPersonSubformComponent } from './edit-person-subform/edit-person-subform.component';

@NgModule({
  imports: [SharedModule, CoreEntitiesModule],
  declarations: [EditPersonSubformComponent],
  entryComponents: [],
  providers: [],
  exports: [EditPersonSubformComponent, SharedModule, CoreEntitiesModule],
})
export class CustomFormsModule {}
