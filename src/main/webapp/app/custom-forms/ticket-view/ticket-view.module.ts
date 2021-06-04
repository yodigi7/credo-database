import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { TICKET_LOOKUP_ROUTE } from './ticket-view.route';
import { ticketViewComponent } from './ticket-view.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([TICKET_LOOKUP_ROUTE], { useHash: true })],
  declarations: [ticketViewComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CredoDatabaseAppticketViewModule {}
