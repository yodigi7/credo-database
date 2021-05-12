import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHouseDetails } from '../house-details.model';

@Component({
  selector: 'jhi-house-details-detail',
  templateUrl: './house-details-detail.component.html',
})
export class HouseDetailsDetailComponent implements OnInit {
  houseDetails: IHouseDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ houseDetails }) => {
      this.houseDetails = houseDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
