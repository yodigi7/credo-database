import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonEmail, PersonEmail } from '../person-email.model';
import { PersonEmailService } from '../service/person-email.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-person-email-update',
  templateUrl: './person-email-update.component.html',
})
export class PersonEmailUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    email: [null, [Validators.required]],
    type: [],
    emailNewsletterSubscription: [],
    emailEventNotificationSubscription: [],
    person: [],
  });

  constructor(
    protected personEmailService: PersonEmailService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personEmail }) => {
      this.updateForm(personEmail);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personEmail = this.createFromForm();
    if (personEmail.id !== undefined) {
      this.subscribeToSaveResponse(this.personEmailService.update(personEmail));
    } else {
      this.subscribeToSaveResponse(this.personEmailService.create(personEmail));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonEmail>>): void {
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

  protected updateForm(personEmail: IPersonEmail): void {
    this.editForm.patchValue({
      id: personEmail.id,
      email: personEmail.email,
      type: personEmail.type,
      emailNewsletterSubscription: personEmail.emailNewsletterSubscription,
      emailEventNotificationSubscription: personEmail.emailEventNotificationSubscription,
      person: personEmail.person,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, personEmail.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IPersonEmail {
    return {
      ...new PersonEmail(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      type: this.editForm.get(['type'])!.value,
      emailNewsletterSubscription: this.editForm.get(['emailNewsletterSubscription'])!.value,
      emailEventNotificationSubscription: this.editForm.get(['emailEventNotificationSubscription'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
