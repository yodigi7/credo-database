import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrganizationEmailComponent } from './list/organization-email.component';
import { OrganizationEmailDetailComponent } from './detail/organization-email-detail.component';
import { OrganizationEmailUpdateComponent } from './update/organization-email-update.component';
import { OrganizationEmailDeleteDialogComponent } from './delete/organization-email-delete-dialog.component';
import { OrganizationEmailRoutingModule } from './route/organization-email-routing.module';

@NgModule({
  imports: [SharedModule, OrganizationEmailRoutingModule],
  declarations: [
    OrganizationEmailComponent,
    OrganizationEmailDetailComponent,
    OrganizationEmailUpdateComponent,
    OrganizationEmailDeleteDialogComponent,
  ],
  entryComponents: [OrganizationEmailDeleteDialogComponent],
})
export class OrganizationEmailModule {}
