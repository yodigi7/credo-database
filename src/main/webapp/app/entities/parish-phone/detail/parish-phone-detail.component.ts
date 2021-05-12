import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParishPhone } from '../parish-phone.model';

@Component({
  selector: 'jhi-parish-phone-detail',
  templateUrl: './parish-phone-detail.component.html',
})
export class ParishPhoneDetailComponent implements OnInit {
  parishPhone: IParishPhone | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parishPhone }) => {
      this.parishPhone = parishPhone;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
