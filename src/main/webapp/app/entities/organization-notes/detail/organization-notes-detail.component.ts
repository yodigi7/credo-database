import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationNotes } from '../organization-notes.model';

@Component({
  selector: 'jhi-organization-notes-detail',
  templateUrl: './organization-notes-detail.component.html',
})
export class OrganizationNotesDetailComponent implements OnInit {
  organizationNotes: IOrganizationNotes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationNotes }) => {
      this.organizationNotes = organizationNotes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
