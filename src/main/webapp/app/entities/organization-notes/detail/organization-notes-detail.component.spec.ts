import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganizationNotesDetailComponent } from './organization-notes-detail.component';

describe('Component Tests', () => {
  describe('OrganizationNotes Management Detail Component', () => {
    let comp: OrganizationNotesDetailComponent;
    let fixture: ComponentFixture<OrganizationNotesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrganizationNotesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ organizationNotes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrganizationNotesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrganizationNotesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load organizationNotes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.organizationNotes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
