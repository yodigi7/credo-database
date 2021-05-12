import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonPhoneDetailComponent } from './person-phone-detail.component';

describe('Component Tests', () => {
  describe('PersonPhone Management Detail Component', () => {
    let comp: PersonPhoneDetailComponent;
    let fixture: ComponentFixture<PersonPhoneDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonPhoneDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personPhone: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonPhoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonPhoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personPhone on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personPhone).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
