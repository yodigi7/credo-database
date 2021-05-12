import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembershipLevel } from '../membership-level.model';
import { MembershipLevelService } from '../service/membership-level.service';

@Component({
  templateUrl: './membership-level-delete-dialog.component.html',
})
export class MembershipLevelDeleteDialogComponent {
  membershipLevel?: IMembershipLevel;

  constructor(protected membershipLevelService: MembershipLevelService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.membershipLevelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
