import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHouseAddress } from '../house-address.model';

@Component({
  selector: 'jhi-house-address-detail',
  templateUrl: './house-address-detail.component.html',
})
export class HouseAddressDetailComponent implements OnInit {
  houseAddress: IHouseAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ houseAddress }) => {
      this.houseAddress = houseAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
