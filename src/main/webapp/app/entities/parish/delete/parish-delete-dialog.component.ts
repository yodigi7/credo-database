import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParish } from '../parish.model';
import { ParishService } from '../service/parish.service';

@Component({
  templateUrl: './parish-delete-dialog.component.html',
})
export class ParishDeleteDialogComponent {
  parish?: IParish;

  constructor(protected parishService: ParishService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parishService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
