import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelationship } from '../relationship.model';
import { RelationshipService } from '../service/relationship.service';

@Component({
  templateUrl: './relationship-delete-dialog.component.html',
})
export class RelationshipDeleteDialogComponent {
  relationship?: IRelationship;

  constructor(protected relationshipService: RelationshipService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.relationshipService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
