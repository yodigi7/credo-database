import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonNotesDetailComponent } from './person-notes-detail.component';

describe('Component Tests', () => {
  describe('PersonNotes Management Detail Component', () => {
    let comp: PersonNotesDetailComponent;
    let fixture: ComponentFixture<PersonNotesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonNotesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personNotes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonNotesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonNotesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personNotes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personNotes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
