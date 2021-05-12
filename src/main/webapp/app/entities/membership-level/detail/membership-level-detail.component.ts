import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMembershipLevel } from '../membership-level.model';

@Component({
  selector: 'jhi-membership-level-detail',
  templateUrl: './membership-level-detail.component.html',
})
export class MembershipLevelDetailComponent implements OnInit {
  membershipLevel: IMembershipLevel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membershipLevel }) => {
      this.membershipLevel = membershipLevel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
