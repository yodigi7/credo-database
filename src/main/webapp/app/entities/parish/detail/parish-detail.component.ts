import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParish } from '../parish.model';

@Component({
  selector: 'jhi-parish-detail',
  templateUrl: './parish-detail.component.html',
})
export class ParishDetailComponent implements OnInit {
  parish: IParish | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parish }) => {
      this.parish = parish;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
