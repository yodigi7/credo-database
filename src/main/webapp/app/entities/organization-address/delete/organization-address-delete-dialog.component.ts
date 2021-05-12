import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizationAddress } from '../organization-address.model';
import { OrganizationAddressService } from '../service/organization-address.service';

@Component({
  templateUrl: './organization-address-delete-dialog.component.html',
})
export class OrganizationAddressDeleteDialogComponent {
  organizationAddress?: IOrganizationAddress;

  constructor(protected organizationAddressService: OrganizationAddressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
