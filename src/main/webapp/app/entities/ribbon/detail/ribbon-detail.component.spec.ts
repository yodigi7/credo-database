import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RibbonDetailComponent } from './ribbon-detail.component';

describe('Component Tests', () => {
  describe('Ribbon Management Detail Component', () => {
    let comp: RibbonDetailComponent;
    let fixture: ComponentFixture<RibbonDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RibbonDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ribbon: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RibbonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RibbonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ribbon on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ribbon).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
