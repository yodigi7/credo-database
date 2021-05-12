import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrganizationPhone, OrganizationPhone } from '../organization-phone.model';
import { OrganizationPhoneService } from '../service/organization-phone.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  selector: 'jhi-organization-phone-update',
  templateUrl: './organization-phone-update.component.html',
})
export class OrganizationPhoneUpdateComponent implements OnInit {
  isSaving = false;

  organizationsSharedCollection: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    phoneNumber: [null, [Validators.required, Validators.minLength(7), Validators.pattern('.*[0-9]{3}-[0-9]{4}$')]],
    type: [],
    organization: [],
  });

  constructor(
    protected organizationPhoneService: OrganizationPhoneService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationPhone }) => {
      this.updateForm(organizationPhone);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationPhone = this.createFromForm();
    if (organizationPhone.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationPhoneService.update(organizationPhone));
    } else {
      this.subscribeToSaveResponse(this.organizationPhoneService.create(organizationPhone));
    }
  }

  trackOrganizationById(index: number, item: IOrganization): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationPhone>>): void {
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

  protected updateForm(organizationPhone: IOrganizationPhone): void {
    this.editForm.patchValue({
      id: organizationPhone.id,
      phoneNumber: organizationPhone.phoneNumber,
      type: organizationPhone.type,
      organization: organizationPhone.organization,
    });

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing(
      this.organizationsSharedCollection,
      organizationPhone.organization
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

  protected createFromForm(): IOrganizationPhone {
    return {
      ...new OrganizationPhone(),
      id: this.editForm.get(['id'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      type: this.editForm.get(['type'])!.value,
      organization: this.editForm.get(['organization'])!.value,
    };
  }
}
