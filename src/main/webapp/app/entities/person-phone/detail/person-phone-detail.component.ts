import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonPhone } from '../person-phone.model';

@Component({
  selector: 'jhi-person-phone-detail',
  templateUrl: './person-phone-detail.component.html',
})
export class PersonPhoneDetailComponent implements OnInit {
  personPhone: IPersonPhone | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personPhone }) => {
      this.personPhone = personPhone;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
