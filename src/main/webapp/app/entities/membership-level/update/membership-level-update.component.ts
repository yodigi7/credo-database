import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMembershipLevel, MembershipLevel } from '../membership-level.model';
import { MembershipLevelService } from '../service/membership-level.service';

@Component({
  selector: 'jhi-membership-level-update',
  templateUrl: './membership-level-update.component.html',
})
export class MembershipLevelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    level: [null, [Validators.required]],
  });

  constructor(
    protected membershipLevelService: MembershipLevelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membershipLevel }) => {
      this.updateForm(membershipLevel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membershipLevel = this.createFromForm();
    if (membershipLevel.id !== undefined) {
      this.subscribeToSaveResponse(this.membershipLevelService.update(membershipLevel));
    } else {
      this.subscribeToSaveResponse(this.membershipLevelService.create(membershipLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembershipLevel>>): void {
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

  protected updateForm(membershipLevel: IMembershipLevel): void {
    this.editForm.patchValue({
      id: membershipLevel.id,
      level: membershipLevel.level,
    });
  }

  protected createFromForm(): IMembershipLevel {
    return {
      ...new MembershipLevel(),
      id: this.editForm.get(['id'])!.value,
      level: this.editForm.get(['level'])!.value,
    };
  }
}
