import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrganizationEmail, OrganizationEmail } from '../organization-email.model';
import { OrganizationEmailService } from '../service/organization-email.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  selector: 'jhi-organization-email-update',
  templateUrl: './organization-email-update.component.html',
})
export class OrganizationEmailUpdateComponent implements OnInit {
  isSaving = false;

  organizationsSharedCollection: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    email: [null, [Validators.required]],
    type: [],
    emailNewsletterSubscription: [],
    emailEventNotificationSubscription: [],
    organization: [],
  });

  constructor(
    protected organizationEmailService: OrganizationEmailService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationEmail }) => {
      this.updateForm(organizationEmail);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationEmail = this.createFromForm();
    if (organizationEmail.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationEmailService.update(organizationEmail));
    } else {
      this.subscribeToSaveResponse(this.organizationEmailService.create(organizationEmail));
    }
  }

  trackOrganizationById(index: number, item: IOrganization): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationEmail>>): void {
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

  protected updateForm(organizationEmail: IOrganizationEmail): void {
    this.editForm.patchValue({
      id: organizationEmail.id,
      email: organizationEmail.email,
      type: organizationEmail.type,
      emailNewsletterSubscription: organizationEmail.emailNewsletterSubscription,
      emailEventNotificationSubscription: organizationEmail.emailEventNotificationSubscription,
      organization: organizationEmail.organization,
    });

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing(
      this.organizationsSharedCollection,
      organizationEmail.organization
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

  protected createFromForm(): IOrganizationEmail {
    return {
      ...new OrganizationEmail(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      type: this.editForm.get(['type'])!.value,
      emailNewsletterSubscription: this.editForm.get(['emailNewsletterSubscription'])!.value,
      emailEventNotificationSubscription: this.editForm.get(['emailEventNotificationSubscription'])!.value,
      organization: this.editForm.get(['organization'])!.value,
    };
  }
}
