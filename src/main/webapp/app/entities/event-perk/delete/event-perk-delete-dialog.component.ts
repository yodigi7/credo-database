import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventPerk } from '../event-perk.model';
import { EventPerkService } from '../service/event-perk.service';

@Component({
  templateUrl: './event-perk-delete-dialog.component.html',
})
export class EventPerkDeleteDialogComponent {
  eventPerk?: IEventPerk;

  constructor(protected eventPerkService: EventPerkService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventPerkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
