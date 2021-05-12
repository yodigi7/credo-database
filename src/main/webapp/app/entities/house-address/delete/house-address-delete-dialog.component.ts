import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHouseAddress } from '../house-address.model';
import { HouseAddressService } from '../service/house-address.service';

@Component({
  templateUrl: './house-address-delete-dialog.component.html',
})
export class HouseAddressDeleteDialogComponent {
  houseAddress?: IHouseAddress;

  constructor(protected houseAddressService: HouseAddressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.houseAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
