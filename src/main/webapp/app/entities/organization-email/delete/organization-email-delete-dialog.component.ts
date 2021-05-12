import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizationEmail } from '../organization-email.model';
import { OrganizationEmailService } from '../service/organization-email.service';

@Component({
  templateUrl: './organization-email-delete-dialog.component.html',
})
export class OrganizationEmailDeleteDialogComponent {
  organizationEmail?: IOrganizationEmail;

  constructor(protected organizationEmailService: OrganizationEmailService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationEmailService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
