import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IParish, Parish } from '../parish.model';
import { ParishService } from '../service/parish.service';

@Component({
  selector: 'jhi-parish-update',
  templateUrl: './parish-update.component.html',
})
export class ParishUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected parishService: ParishService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parish }) => {
      this.updateForm(parish);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parish = this.createFromForm();
    if (parish.id !== undefined) {
      this.subscribeToSaveResponse(this.parishService.update(parish));
    } else {
      this.subscribeToSaveResponse(this.parishService.create(parish));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParish>>): void {
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

  protected updateForm(parish: IParish): void {
    this.editForm.patchValue({
      id: parish.id,
      name: parish.name,
    });
  }

  protected createFromForm(): IParish {
    return {
      ...new Parish(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
