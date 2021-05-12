import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonPhone, PersonPhone } from '../person-phone.model';
import { PersonPhoneService } from '../service/person-phone.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-person-phone-update',
  templateUrl: './person-phone-update.component.html',
})
export class PersonPhoneUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    phoneNumber: [null, [Validators.required, Validators.minLength(7), Validators.pattern('.*[0-9]{3}-[0-9]{4}$')]],
    type: [],
    person: [],
  });

  constructor(
    protected personPhoneService: PersonPhoneService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personPhone }) => {
      this.updateForm(personPhone);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personPhone = this.createFromForm();
    if (personPhone.id !== undefined) {
      this.subscribeToSaveResponse(this.personPhoneService.update(personPhone));
    } else {
      this.subscribeToSaveResponse(this.personPhoneService.create(personPhone));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonPhone>>): void {
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

  protected updateForm(personPhone: IPersonPhone): void {
    this.editForm.patchValue({
      id: personPhone.id,
      phoneNumber: personPhone.phoneNumber,
      type: personPhone.type,
      person: personPhone.person,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, personPhone.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IPersonPhone {
    return {
      ...new PersonPhone(),
      id: this.editForm.get(['id'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      type: this.editForm.get(['type'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
