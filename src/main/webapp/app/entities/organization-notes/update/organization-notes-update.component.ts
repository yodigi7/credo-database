import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrganizationNotes, OrganizationNotes } from '../organization-notes.model';
import { OrganizationNotesService } from '../service/organization-notes.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  selector: 'jhi-organization-notes-update',
  templateUrl: './organization-notes-update.component.html',
})
export class OrganizationNotesUpdateComponent implements OnInit {
  isSaving = false;

  organizationsCollection: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    notes: [],
    organization: [],
  });

  constructor(
    protected organizationNotesService: OrganizationNotesService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationNotes }) => {
      this.updateForm(organizationNotes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationNotes = this.createFromForm();
    if (organizationNotes.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationNotesService.update(organizationNotes));
    } else {
      this.subscribeToSaveResponse(this.organizationNotesService.create(organizationNotes));
    }
  }

  trackOrganizationById(index: number, item: IOrganization): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationNotes>>): void {
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

  protected updateForm(organizationNotes: IOrganizationNotes): void {
    this.editForm.patchValue({
      id: organizationNotes.id,
      notes: organizationNotes.notes,
      organization: organizationNotes.organization,
    });

    this.organizationsCollection = this.organizationService.addOrganizationToCollectionIfMissing(
      this.organizationsCollection,
      organizationNotes.organization
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query({ 'notesId.specified': 'false' })
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing(organizations, this.editForm.get('organization')!.value)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsCollection = organizations));
  }

  protected createFromForm(): IOrganizationNotes {
    return {
      ...new OrganizationNotes(),
      id: this.editForm.get(['id'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      organization: this.editForm.get(['organization'])!.value,
    };
  }
}
