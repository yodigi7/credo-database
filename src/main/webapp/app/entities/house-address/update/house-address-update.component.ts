import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHouseAddress, HouseAddress } from '../house-address.model';
import { HouseAddressService } from '../service/house-address.service';
import { IHouseDetails } from 'app/entities/house-details/house-details.model';
import { HouseDetailsService } from 'app/entities/house-details/service/house-details.service';

@Component({
  selector: 'jhi-house-address-update',
  templateUrl: './house-address-update.component.html',
})
export class HouseAddressUpdateComponent implements OnInit {
  isSaving = false;

  houseDetailsSharedCollection: IHouseDetails[] = [];

  editForm = this.fb.group({
    id: [],
    streetAddress: [],
    city: [],
    state: [],
    zipcode: [],
    type: [],
    mailNewsletterSubscription: [],
    mailEventNotificationSubscription: [],
    houseDetails: [],
  });

  constructor(
    protected houseAddressService: HouseAddressService,
    protected houseDetailsService: HouseDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ houseAddress }) => {
      this.updateForm(houseAddress);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const houseAddress = this.createFromForm();
    if (houseAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.houseAddressService.update(houseAddress));
    } else {
      this.subscribeToSaveResponse(this.houseAddressService.create(houseAddress));
    }
  }

  trackHouseDetailsById(index: number, item: IHouseDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHouseAddress>>): void {
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

  protected updateForm(houseAddress: IHouseAddress): void {
    this.editForm.patchValue({
      id: houseAddress.id,
      streetAddress: houseAddress.streetAddress,
      city: houseAddress.city,
      state: houseAddress.state,
      zipcode: houseAddress.zipcode,
      type: houseAddress.type,
      mailNewsletterSubscription: houseAddress.mailNewsletterSubscription,
      mailEventNotificationSubscription: houseAddress.mailEventNotificationSubscription,
      houseDetails: houseAddress.houseDetails,
    });

    this.houseDetailsSharedCollection = this.houseDetailsService.addHouseDetailsToCollectionIfMissing(
      this.houseDetailsSharedCollection,
      houseAddress.houseDetails
    );
  }

  protected loadRelationshipsOptions(): void {
    this.houseDetailsService
      .query()
      .pipe(map((res: HttpResponse<IHouseDetails[]>) => res.body ?? []))
      .pipe(
        map((houseDetails: IHouseDetails[]) =>
          this.houseDetailsService.addHouseDetailsToCollectionIfMissing(houseDetails, this.editForm.get('houseDetails')!.value)
        )
      )
      .subscribe((houseDetails: IHouseDetails[]) => (this.houseDetailsSharedCollection = houseDetails));
  }

  protected createFromForm(): IHouseAddress {
    return {
      ...new HouseAddress(),
      id: this.editForm.get(['id'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      zipcode: this.editForm.get(['zipcode'])!.value,
      type: this.editForm.get(['type'])!.value,
      mailNewsletterSubscription: this.editForm.get(['mailNewsletterSubscription'])!.value,
      mailEventNotificationSubscription: this.editForm.get(['mailEventNotificationSubscription'])!.value,
      houseDetails: this.editForm.get(['houseDetails'])!.value,
    };
  }
}
