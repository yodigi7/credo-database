import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonEmail } from '../person-email.model';

@Component({
  selector: 'jhi-person-email-detail',
  templateUrl: './person-email-detail.component.html',
})
export class PersonEmailDetailComponent implements OnInit {
  personEmail: IPersonEmail | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personEmail }) => {
      this.personEmail = personEmail;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
