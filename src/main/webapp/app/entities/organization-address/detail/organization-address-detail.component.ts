import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationAddress } from '../organization-address.model';

@Component({
  selector: 'jhi-organization-address-detail',
  templateUrl: './organization-address-detail.component.html',
})
export class OrganizationAddressDetailComponent implements OnInit {
  organizationAddress: IOrganizationAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationAddress }) => {
      this.organizationAddress = organizationAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
