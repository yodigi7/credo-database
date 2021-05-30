import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { ADD_TRANSACTION_ROUTE } from './add-transaction.route';
import { AddTransactionComponent } from './add-transaction.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([ADD_TRANSACTION_ROUTE], { useHash: true })],
  declarations: [AddTransactionComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CredoDatabaseAppAddTransactionModule {}
