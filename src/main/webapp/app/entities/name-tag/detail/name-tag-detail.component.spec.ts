import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NameTagDetailComponent } from './name-tag-detail.component';

describe('Component Tests', () => {
  describe('NameTag Management Detail Component', () => {
    let comp: NameTagDetailComponent;
    let fixture: ComponentFixture<NameTagDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NameTagDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nameTag: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NameTagDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NameTagDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nameTag on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nameTag).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
