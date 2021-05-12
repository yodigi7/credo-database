import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHouseDetails } from '../house-details.model';
import { HouseDetailsService } from '../service/house-details.service';

@Component({
  templateUrl: './house-details-delete-dialog.component.html',
})
export class HouseDetailsDeleteDialogComponent {
  houseDetails?: IHouseDetails;

  constructor(protected houseDetailsService: HouseDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.houseDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
