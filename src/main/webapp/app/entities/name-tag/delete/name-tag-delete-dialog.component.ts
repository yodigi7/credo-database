import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INameTag } from '../name-tag.model';
import { NameTagService } from '../service/name-tag.service';

@Component({
  templateUrl: './name-tag-delete-dialog.component.html',
})
export class NameTagDeleteDialogComponent {
  nameTag?: INameTag;

  constructor(protected nameTagService: NameTagService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nameTagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
