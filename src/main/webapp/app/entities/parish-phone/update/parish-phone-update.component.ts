import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IParishPhone, ParishPhone } from '../parish-phone.model';
import { ParishPhoneService } from '../service/parish-phone.service';
import { IParish } from 'app/entities/parish/parish.model';
import { ParishService } from 'app/entities/parish/service/parish.service';

@Component({
  selector: 'jhi-parish-phone-update',
  templateUrl: './parish-phone-update.component.html',
})
export class ParishPhoneUpdateComponent implements OnInit {
  isSaving = false;

  parishesSharedCollection: IParish[] = [];

  editForm = this.fb.group({
    id: [],
    phoneNumber: [null, [Validators.required, Validators.minLength(10), Validators.pattern('^([0-9]{3}) [0-9]{3}-[0-9]{4}$')]],
    type: [],
    parish: [],
  });

  constructor(
    protected parishPhoneService: ParishPhoneService,
    protected parishService: ParishService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parishPhone }) => {
      this.updateForm(parishPhone);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parishPhone = this.createFromForm();
    if (parishPhone.id !== undefined) {
      this.subscribeToSaveResponse(this.parishPhoneService.update(parishPhone));
    } else {
      this.subscribeToSaveResponse(this.parishPhoneService.create(parishPhone));
    }
  }

  trackParishById(index: number, item: IParish): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParishPhone>>): void {
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

  protected updateForm(parishPhone: IParishPhone): void {
    this.editForm.patchValue({
      id: parishPhone.id,
      phoneNumber: parishPhone.phoneNumber,
      type: parishPhone.type,
      parish: parishPhone.parish,
    });

    this.parishesSharedCollection = this.parishService.addParishToCollectionIfMissing(this.parishesSharedCollection, parishPhone.parish);
  }

  protected loadRelationshipsOptions(): void {
    this.parishService
      .query()
      .pipe(map((res: HttpResponse<IParish[]>) => res.body ?? []))
      .pipe(map((parishes: IParish[]) => this.parishService.addParishToCollectionIfMissing(parishes, this.editForm.get('parish')!.value)))
      .subscribe((parishes: IParish[]) => (this.parishesSharedCollection = parishes));
  }

  protected createFromForm(): IParishPhone {
    return {
      ...new ParishPhone(),
      id: this.editForm.get(['id'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      type: this.editForm.get(['type'])!.value,
      parish: this.editForm.get(['parish'])!.value,
    };
  }
}
