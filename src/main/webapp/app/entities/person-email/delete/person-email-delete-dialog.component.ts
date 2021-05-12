import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonEmail } from '../person-email.model';
import { PersonEmailService } from '../service/person-email.service';

@Component({
  templateUrl: './person-email-delete-dialog.component.html',
})
export class PersonEmailDeleteDialogComponent {
  personEmail?: IPersonEmail;

  constructor(protected personEmailService: PersonEmailService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personEmailService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
