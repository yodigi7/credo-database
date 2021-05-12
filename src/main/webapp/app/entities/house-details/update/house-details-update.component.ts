import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHouseDetails, HouseDetails } from '../house-details.model';
import { HouseDetailsService } from '../service/house-details.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-house-details-update',
  templateUrl: './house-details-update.component.html',
})
export class HouseDetailsUpdateComponent implements OnInit {
  isSaving = false;

  headOfHousesCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    mailingLabel: [],
    headOfHouse: [],
  });

  constructor(
    protected houseDetailsService: HouseDetailsService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ houseDetails }) => {
      this.updateForm(houseDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const houseDetails = this.createFromForm();
    if (houseDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.houseDetailsService.update(houseDetails));
    } else {
      this.subscribeToSaveResponse(this.houseDetailsService.create(houseDetails));
    }
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHouseDetails>>): void {
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

  protected updateForm(houseDetails: IHouseDetails): void {
    this.editForm.patchValue({
      id: houseDetails.id,
      mailingLabel: houseDetails.mailingLabel,
      headOfHouse: houseDetails.headOfHouse,
    });

    this.headOfHousesCollection = this.personService.addPersonToCollectionIfMissing(this.headOfHousesCollection, houseDetails.headOfHouse);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ 'houseDetailsId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('headOfHouse')!.value)))
      .subscribe((people: IPerson[]) => (this.headOfHousesCollection = people));
  }

  protected createFromForm(): IHouseDetails {
    return {
      ...new HouseDetails(),
      id: this.editForm.get(['id'])!.value,
      mailingLabel: this.editForm.get(['mailingLabel'])!.value,
      headOfHouse: this.editForm.get(['headOfHouse'])!.value,
    };
  }
}
