import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizationPhone } from '../organization-phone.model';
import { OrganizationPhoneService } from '../service/organization-phone.service';

@Component({
  templateUrl: './organization-phone-delete-dialog.component.html',
})
export class OrganizationPhoneDeleteDialogComponent {
  organizationPhone?: IOrganizationPhone;

  constructor(protected organizationPhoneService: OrganizationPhoneService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationPhoneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
