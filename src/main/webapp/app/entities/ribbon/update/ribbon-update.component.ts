import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRibbon, Ribbon } from '../ribbon.model';
import { RibbonService } from '../service/ribbon.service';

@Component({
  selector: 'jhi-ribbon-update',
  templateUrl: './ribbon-update.component.html',
})
export class RibbonUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected ribbonService: RibbonService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ribbon }) => {
      this.updateForm(ribbon);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ribbon = this.createFromForm();
    if (ribbon.id !== undefined) {
      this.subscribeToSaveResponse(this.ribbonService.update(ribbon));
    } else {
      this.subscribeToSaveResponse(this.ribbonService.create(ribbon));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRibbon>>): void {
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

  protected updateForm(ribbon: IRibbon): void {
    this.editForm.patchValue({
      id: ribbon.id,
      name: ribbon.name,
    });
  }

  protected createFromForm(): IRibbon {
    return {
      ...new Ribbon(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
