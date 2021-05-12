import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParishPhoneDetailComponent } from './parish-phone-detail.component';

describe('Component Tests', () => {
  describe('ParishPhone Management Detail Component', () => {
    let comp: ParishPhoneDetailComponent;
    let fixture: ComponentFixture<ParishPhoneDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ParishPhoneDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ parishPhone: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ParishPhoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParishPhoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load parishPhone on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parishPhone).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
