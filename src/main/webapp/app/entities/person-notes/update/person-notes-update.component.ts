import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonNotes, PersonNotes } from '../person-notes.model';
import { PersonNotesService } from '../service/person-notes.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-person-notes-update',
  templateUrl: './person-notes-update.component.html',
})
export class PersonNotesUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    notes: [],
    person: [],
  });

  constructor(
    protected personNotesService: PersonNotesService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personNotes }) => {
      this.updateForm(personNotes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personNotes = this.createFromForm();
    if (personNotes.id !== undefined) {
      this.subscribeToSaveResponse(this.personNotesService.update(personNotes));
    } else {
      this.subscribeToSaveResponse(this.personNotesService.create(personNotes));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonNotes>>): void {
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

  protected updateForm(personNotes: IPersonNotes): void {
    this.editForm.patchValue({
      id: personNotes.id,
      notes: personNotes.notes,
      person: personNotes.person,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, personNotes.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ 'notesId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));
  }

  protected createFromForm(): IPersonNotes {
    return {
      ...new PersonNotes(),
      id: this.editForm.get(['id'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
