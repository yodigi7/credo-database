import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IConfig, NgxMaskModule } from 'ngx-mask';
import { CoreEntitiesModule } from '../entities/core-entities.module';
import { EditPersonSubformComponent } from './edit-person-subform/edit-person-subform.component';

export const options: Partial<IConfig> | (() => Partial<IConfig>) | null = null;

@NgModule({
  imports: [SharedModule, CoreEntitiesModule, NgxMaskModule.forRoot()],
  declarations: [EditPersonSubformComponent],
  entryComponents: [],
  providers: [],
  exports: [EditPersonSubformComponent, SharedModule, CoreEntitiesModule],
})
export class CustomFormsModule {}
