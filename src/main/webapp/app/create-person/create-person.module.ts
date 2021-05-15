import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { CREATE_PERSON_ROUTE } from '../create-person/create-person.route';
import { CreatePersonComponent } from '../create-person/create-person.component';
import { CoreEntitiesModule } from '../entities/core-entities.module';

@NgModule({
  imports: [SharedModule, CoreEntitiesModule, RouterModule.forRoot([CREATE_PERSON_ROUTE], { useHash: true })],
  declarations: [CreatePersonComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CredoDatabaseAppCreatePersonModule {}
