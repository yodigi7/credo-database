import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MembershipLevelDetailComponent } from './membership-level-detail.component';

describe('Component Tests', () => {
  describe('MembershipLevel Management Detail Component', () => {
    let comp: MembershipLevelDetailComponent;
    let fixture: ComponentFixture<MembershipLevelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MembershipLevelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ membershipLevel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MembershipLevelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MembershipLevelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load membershipLevel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.membershipLevel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
