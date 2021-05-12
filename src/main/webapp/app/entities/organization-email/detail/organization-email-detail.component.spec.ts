import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganizationEmailDetailComponent } from './organization-email-detail.component';

describe('Component Tests', () => {
  describe('OrganizationEmail Management Detail Component', () => {
    let comp: OrganizationEmailDetailComponent;
    let fixture: ComponentFixture<OrganizationEmailDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrganizationEmailDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ organizationEmail: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrganizationEmailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrganizationEmailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load organizationEmail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.organizationEmail).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
