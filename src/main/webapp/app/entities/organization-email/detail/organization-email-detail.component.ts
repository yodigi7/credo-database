import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationEmail } from '../organization-email.model';

@Component({
  selector: 'jhi-organization-email-detail',
  templateUrl: './organization-email-detail.component.html',
})
export class OrganizationEmailDetailComponent implements OnInit {
  organizationEmail: IOrganizationEmail | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationEmail }) => {
      this.organizationEmail = organizationEmail;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
