import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonNotes } from '../person-notes.model';

@Component({
  selector: 'jhi-person-notes-detail',
  templateUrl: './person-notes-detail.component.html',
})
export class PersonNotesDetailComponent implements OnInit {
  personNotes: IPersonNotes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personNotes }) => {
      this.personNotes = personNotes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
