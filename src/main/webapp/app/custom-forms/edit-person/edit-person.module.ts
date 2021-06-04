import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { EditPersonComponent } from './edit-person.component';
import { CoreEntitiesModule } from 'app/entities/core-entities.module';
import { CustomFormsModule } from 'app/custom-forms/custom-forms.module';
import { editPersonRoutes } from './edit-person.route';

@NgModule({
  imports: [SharedModule, CoreEntitiesModule, CustomFormsModule, RouterModule.forChild(editPersonRoutes)],
  declarations: [EditPersonComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CredoDatabaseAppEditPersonModule {}
