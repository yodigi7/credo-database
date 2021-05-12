import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IParishEmail, ParishEmail } from '../parish-email.model';
import { ParishEmailService } from '../service/parish-email.service';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';

@Component({
  selector: 'jhi-parish-email-update',
  templateUrl: './parish-email-update.component.html',
})
export class ParishEmailUpdateComponent implements OnInit {
  isSaving = false;

  parishesSharedCollection: IParish[] = [];

  editForm = this.fb.group({
    id: [],
    email: [null, [Validators.required]],
    type: [],
    emailNewsletterSubscription: [],
    emailEventNotificationSubscription: [],
    parish: [],
  });

  constructor(
    protected parishEmailService: ParishEmailService,
    protected parishService: ParishService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parishEmail }) => {
      this.updateForm(parishEmail);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parishEmail = this.createFromForm();
    if (parishEmail.id !== undefined) {
      this.subscribeToSaveResponse(this.parishEmailService.update(parishEmail));
    } else {
      this.subscribeToSaveResponse(this.parishEmailService.create(parishEmail));
    }
  }

  trackParishById(index: number, item: IParish): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParishEmail>>): void {
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

  protected updateForm(parishEmail: IParishEmail): void {
    this.editForm.patchValue({
      id: parishEmail.id,
      email: parishEmail.email,
      type: parishEmail.type,
      emailNewsletterSubscription: parishEmail.emailNewsletterSubscription,
      emailEventNotificationSubscription: parishEmail.emailEventNotificationSubscription,
      parish: parishEmail.parish,
    });

    this.parishesSharedCollection = this.parishService.addParishToCollectionIfMissing(this.parishesSharedCollection, parishEmail.parish);
  }

  protected loadRelationshipsOptions(): void {
    this.parishService
      .query()
      .pipe(map((res: HttpResponse<IParish[]>) => res.body ?? []))
      .pipe(map((parishes: IParish[]) => this.parishService.addParishToCollectionIfMissing(parishes, this.editForm.get('parish')!.value)))
      .subscribe((parishes: IParish[]) => (this.parishesSharedCollection = parishes));
  }

  protected createFromForm(): IParishEmail {
    return {
      ...new ParishEmail(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      type: this.editForm.get(['type'])!.value,
      emailNewsletterSubscription: this.editForm.get(['emailNewsletterSubscription'])!.value,
      emailEventNotificationSubscription: this.editForm.get(['emailEventNotificationSubscription'])!.value,
      parish: this.editForm.get(['parish'])!.value,
    };
  }
}
