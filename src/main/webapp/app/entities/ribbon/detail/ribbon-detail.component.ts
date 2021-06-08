import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRibbon } from '../ribbon.model';

@Component({
  selector: 'jhi-ribbon-detail',
  templateUrl: './ribbon-detail.component.html',
})
export class RibbonDetailComponent implements OnInit {
  ribbon: IRibbon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ribbon }) => {
      this.ribbon = ribbon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
