import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonNotes } from '../person-notes.model';
import { PersonNotesService } from '../service/person-notes.service';

@Component({
  templateUrl: './person-notes-delete-dialog.component.html',
})
export class PersonNotesDeleteDialogComponent {
  personNotes?: IPersonNotes;

  constructor(protected personNotesService: PersonNotesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personNotesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
