import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizationNotes } from '../organization-notes.model';
import { OrganizationNotesService } from '../service/organization-notes.service';

@Component({
  templateUrl: './organization-notes-delete-dialog.component.html',
})
export class OrganizationNotesDeleteDialogComponent {
  organizationNotes?: IOrganizationNotes;

  constructor(protected organizationNotesService: OrganizationNotesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationNotesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
