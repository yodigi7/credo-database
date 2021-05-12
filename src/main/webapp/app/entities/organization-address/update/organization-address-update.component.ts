import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrganizationAddress, OrganizationAddress } from '../organization-address.model';
import { OrganizationAddressService } from '../service/organization-address.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  selector: 'jhi-organization-address-update',
  templateUrl: './organization-address-update.component.html',
})
export class OrganizationAddressUpdateComponent implements OnInit {
  isSaving = false;

  organizationsSharedCollection: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    streetAddress: [],
    city: [],
    state: [],
    zipcode: [],
    organization: [],
  });

  constructor(
    protected organizationAddressService: OrganizationAddressService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationAddress }) => {
      this.updateForm(organizationAddress);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationAddress = this.createFromForm();
    if (organizationAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationAddressService.update(organizationAddress));
    } else {
      this.subscribeToSaveResponse(this.organizationAddressService.create(organizationAddress));
    }
  }

  trackOrganizationById(index: number, item: IOrganization): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationAddress>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(organizationAddress: IOrganizationAddress): void {
    this.editForm.patchValue({
      id: organizationAddress.id,
      streetAddress: organizationAddress.streetAddress,
      city: organizationAddress.city,
      state: organizationAddress.state,
      zipcode: organizationAddress.zipcode,
      organization: organizationAddress.organization,
    });

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing(
      this.organizationsSharedCollection,
      organizationAddress.organization
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing(organizations, this.editForm.get('organization')!.value)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }

  protected createFromForm(): IOrganizationAddress {
    return {
      ...new OrganizationAddress(),
      id: this.editForm.get(['id'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      zipcode: this.editForm.get(['zipcode'])!.value,
      organization: this.editForm.get(['organization'])!.value,
    };
  }
}
