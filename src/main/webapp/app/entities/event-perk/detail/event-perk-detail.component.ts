import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventPerk } from '../event-perk.model';

@Component({
  selector: 'jhi-event-perk-detail',
  templateUrl: './event-perk-detail.component.html',
})
export class EventPerkDetailComponent implements OnInit {
  eventPerk: IEventPerk | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventPerk }) => {
      this.eventPerk = eventPerk;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
