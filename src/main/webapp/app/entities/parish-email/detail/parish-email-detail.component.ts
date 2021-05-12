import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParishEmail } from '../parish-email.model';

@Component({
  selector: 'jhi-parish-email-detail',
  templateUrl: './parish-email-detail.component.html',
})
export class ParishEmailDetailComponent implements OnInit {
  parishEmail: IParishEmail | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parishEmail }) => {
      this.parishEmail = parishEmail;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
