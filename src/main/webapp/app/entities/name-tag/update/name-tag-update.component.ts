import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INameTag, NameTag } from '../name-tag.model';
import { NameTagService } from '../service/name-tag.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';

@Component({
  selector: 'jhi-name-tag-update',
  templateUrl: './name-tag-update.component.html',
})
export class NameTagUpdateComponent implements OnInit {
  isSaving = false;

  ticketsSharedCollection: ITicket[] = [];

  editForm = this.fb.group({
    id: [],
    nameTag: [],
    ticket: [],
  });

  constructor(
    protected nameTagService: NameTagService,
    protected ticketService: TicketService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nameTag }) => {
      this.updateForm(nameTag);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nameTag = this.createFromForm();
    if (nameTag.id !== undefined) {
      this.subscribeToSaveResponse(this.nameTagService.update(nameTag));
    } else {
      this.subscribeToSaveResponse(this.nameTagService.create(nameTag));
    }
  }

  trackTicketById(index: number, item: ITicket): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INameTag>>): void {
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

  protected updateForm(nameTag: INameTag): void {
    this.editForm.patchValue({
      id: nameTag.id,
      nameTag: nameTag.nameTag,
      ticket: nameTag.ticket,
    });

    this.ticketsSharedCollection = this.ticketService.addTicketToCollectionIfMissing(this.ticketsSharedCollection, nameTag.ticket);
  }

  protected loadRelationshipsOptions(): void {
    this.ticketService
      .query()
      .pipe(map((res: HttpResponse<ITicket[]>) => res.body ?? []))
      .pipe(map((tickets: ITicket[]) => this.ticketService.addTicketToCollectionIfMissing(tickets, this.editForm.get('ticket')!.value)))
      .subscribe((tickets: ITicket[]) => (this.ticketsSharedCollection = tickets));
  }

  protected createFromForm(): INameTag {
    return {
      ...new NameTag(),
      id: this.editForm.get(['id'])!.value,
      nameTag: this.editForm.get(['nameTag'])!.value,
      ticket: this.editForm.get(['ticket'])!.value,
    };
  }
}
