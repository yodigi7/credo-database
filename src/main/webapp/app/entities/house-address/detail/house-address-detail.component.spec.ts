import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HouseAddressDetailComponent } from './house-address-detail.component';

describe('Component Tests', () => {
  describe('HouseAddress Management Detail Component', () => {
    let comp: HouseAddressDetailComponent;
    let fixture: ComponentFixture<HouseAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HouseAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ houseAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HouseAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HouseAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load houseAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.houseAddress).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
