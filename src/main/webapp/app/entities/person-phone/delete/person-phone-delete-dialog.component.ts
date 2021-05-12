import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonPhone } from '../person-phone.model';
import { PersonPhoneService } from '../service/person-phone.service';

@Component({
  templateUrl: './person-phone-delete-dialog.component.html',
})
export class PersonPhoneDeleteDialogComponent {
  personPhone?: IPersonPhone;

  constructor(protected personPhoneService: PersonPhoneService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personPhoneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
