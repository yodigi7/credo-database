import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventPerkDetailComponent } from './event-perk-detail.component';

describe('Component Tests', () => {
  describe('EventPerk Management Detail Component', () => {
    let comp: EventPerkDetailComponent;
    let fixture: ComponentFixture<EventPerkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EventPerkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ eventPerk: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EventPerkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventPerkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load eventPerk on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.eventPerk).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
