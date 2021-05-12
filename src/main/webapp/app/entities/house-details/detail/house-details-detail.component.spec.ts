import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HouseDetailsDetailComponent } from './house-details-detail.component';

describe('Component Tests', () => {
  describe('HouseDetails Management Detail Component', () => {
    let comp: HouseDetailsDetailComponent;
    let fixture: ComponentFixture<HouseDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HouseDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ houseDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HouseDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HouseDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load houseDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.houseDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
