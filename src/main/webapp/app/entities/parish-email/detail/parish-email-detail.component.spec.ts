import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParishEmailDetailComponent } from './parish-email-detail.component';

describe('Component Tests', () => {
  describe('ParishEmail Management Detail Component', () => {
    let comp: ParishEmailDetailComponent;
    let fixture: ComponentFixture<ParishEmailDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ParishEmailDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ parishEmail: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ParishEmailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParishEmailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load parishEmail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parishEmail).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
