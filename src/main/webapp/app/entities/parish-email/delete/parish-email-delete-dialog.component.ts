import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParishEmail } from '../parish-email.model';
import { ParishEmailService } from '../service/parish-email.service';

@Component({
  templateUrl: './parish-email-delete-dialog.component.html',
})
export class ParishEmailDeleteDialogComponent {
  parishEmail?: IParishEmail;

  constructor(protected parishEmailService: ParishEmailService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parishEmailService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
