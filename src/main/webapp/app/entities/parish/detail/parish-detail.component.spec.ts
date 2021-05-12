import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParishDetailComponent } from './parish-detail.component';

describe('Component Tests', () => {
  describe('Parish Management Detail Component', () => {
    let comp: ParishDetailComponent;
    let fixture: ComponentFixture<ParishDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ParishDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ parish: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ParishDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParishDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load parish on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parish).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
