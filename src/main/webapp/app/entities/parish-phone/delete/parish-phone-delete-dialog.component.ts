import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParishPhone } from '../parish-phone.model';
import { ParishPhoneService } from '../service/parish-phone.service';

@Component({
  templateUrl: './parish-phone-delete-dialog.component.html',
})
export class ParishPhoneDeleteDialogComponent {
  parishPhone?: IParishPhone;

  constructor(protected parishPhoneService: ParishPhoneService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parishPhoneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
