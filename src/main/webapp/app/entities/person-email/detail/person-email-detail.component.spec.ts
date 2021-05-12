import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonEmailDetailComponent } from './person-email-detail.component';

describe('Component Tests', () => {
  describe('PersonEmail Management Detail Component', () => {
    let comp: PersonEmailDetailComponent;
    let fixture: ComponentFixture<PersonEmailDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonEmailDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personEmail: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonEmailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonEmailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personEmail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personEmail).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
