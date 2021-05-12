import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationPhone } from '../organization-phone.model';

@Component({
  selector: 'jhi-organization-phone-detail',
  templateUrl: './organization-phone-detail.component.html',
})
export class OrganizationPhoneDetailComponent implements OnInit {
  organizationPhone: IOrganizationPhone | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationPhone }) => {
      this.organizationPhone = organizationPhone;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
