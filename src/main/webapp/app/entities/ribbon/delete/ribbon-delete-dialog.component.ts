import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRibbon } from '../ribbon.model';
import { RibbonService } from '../service/ribbon.service';

@Component({
  templateUrl: './ribbon-delete-dialog.component.html',
})
export class RibbonDeleteDialogComponent {
  ribbon?: IRibbon;

  constructor(protected ribbonService: RibbonService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ribbonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
