import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEventPerk, EventPerk } from '../event-perk.model';
import { EventPerkService } from '../service/event-perk.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-event-perk-update',
  templateUrl: './event-perk-update.component.html',
})
export class EventPerkUpdateComponent implements OnInit {
  isSaving = false;

  eventsSharedCollection: IEvent[] = [];
  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    minimumPrice: [],
    event: [],
    person: [],
  });

  constructor(
    protected eventPerkService: EventPerkService,
    protected eventService: EventService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventPerk }) => {
      this.updateForm(eventPerk);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventPerk = this.createFromForm();
    if (eventPerk.id !== undefined) {
      this.subscribeToSaveResponse(this.eventPerkService.update(eventPerk));
    } else {
      this.subscribeToSaveResponse(this.eventPerkService.create(eventPerk));
    }
  }

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventPerk>>): void {
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

  protected updateForm(eventPerk: IEventPerk): void {
    this.editForm.patchValue({
      id: eventPerk.id,
      name: eventPerk.name,
      minimumPrice: eventPerk.minimumPrice,
      event: eventPerk.event,
      person: eventPerk.person,
    });

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, eventPerk.event);
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, eventPerk.person);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IEventPerk {
    return {
      ...new EventPerk(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      minimumPrice: this.editForm.get(['minimumPrice'])!.value,
      event: this.editForm.get(['event'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
