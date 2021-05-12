import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrganization, Organization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';

@Component({
  selector: 'jhi-organization-update',
  templateUrl: './organization-update.component.html',
})
export class OrganizationUpdateComponent implements OnInit {
  isSaving = false;

  parishesSharedCollection: IParish[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    mailingLabel: [],
    parish: [],
  });

  constructor(
    protected organizationService: OrganizationService,
    protected parishService: ParishService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organization }) => {
      this.updateForm(organization);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organization = this.createFromForm();
    if (organization.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationService.update(organization));
    } else {
      this.subscribeToSaveResponse(this.organizationService.create(organization));
    }
  }

  trackParishById(index: number, item: IParish): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganization>>): void {
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

  protected updateForm(organization: IOrganization): void {
    this.editForm.patchValue({
      id: organization.id,
      name: organization.name,
      mailingLabel: organization.mailingLabel,
      parish: organization.parish,
    });

    this.parishesSharedCollection = this.parishService.addParishToCollectionIfMissing(this.parishesSharedCollection, organization.parish);
  }

  protected loadRelationshipsOptions(): void {
    this.parishService
      .query()
      .pipe(map((res: HttpResponse<IParish[]>) => res.body ?? []))
      .pipe(map((parishes: IParish[]) => this.parishService.addParishToCollectionIfMissing(parishes, this.editForm.get('parish')!.value)))
      .subscribe((parishes: IParish[]) => (this.parishesSharedCollection = parishes));
  }

  protected createFromForm(): IOrganization {
    return {
      ...new Organization(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      mailingLabel: this.editForm.get(['mailingLabel'])!.value,
      parish: this.editForm.get(['parish'])!.value,
    };
  }
}
