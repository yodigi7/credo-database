import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRelationship, Relationship } from '../relationship.model';
import { RelationshipService } from '../service/relationship.service';

@Component({
  selector: 'jhi-relationship-update',
  templateUrl: './relationship-update.component.html',
})
export class RelationshipUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    relationship: [],
  });

  constructor(protected relationshipService: RelationshipService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relationship }) => {
      this.updateForm(relationship);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relationship = this.createFromForm();
    if (relationship.id !== undefined) {
      this.subscribeToSaveResponse(this.relationshipService.update(relationship));
    } else {
      this.subscribeToSaveResponse(this.relationshipService.create(relationship));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelationship>>): void {
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

  protected updateForm(relationship: IRelationship): void {
    this.editForm.patchValue({
      id: relationship.id,
      relationship: relationship.relationship,
    });
  }

  protected createFromForm(): IRelationship {
    return {
      ...new Relationship(),
      id: this.editForm.get(['id'])!.value,
      relationship: this.editForm.get(['relationship'])!.value,
    };
  }
}
