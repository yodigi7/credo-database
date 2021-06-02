import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INameTag } from '../name-tag.model';

@Component({
  selector: 'jhi-name-tag-detail',
  templateUrl: './name-tag-detail.component.html',
})
export class NameTagDetailComponent implements OnInit {
  nameTag: INameTag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nameTag }) => {
      this.nameTag = nameTag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
