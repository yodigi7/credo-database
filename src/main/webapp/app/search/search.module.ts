import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { SEARCH_ROUTE } from './search.route';
import { SearchComponent } from './search.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([SEARCH_ROUTE], { useHash: true })],
  declarations: [SearchComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CredoDatabaseAppSearchModule {}
