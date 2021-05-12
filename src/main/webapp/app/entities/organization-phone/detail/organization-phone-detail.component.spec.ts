import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganizationPhoneDetailComponent } from './organization-phone-detail.component';

describe('Component Tests', () => {
  describe('OrganizationPhone Management Detail Component', () => {
    let comp: OrganizationPhoneDetailComponent;
    let fixture: ComponentFixture<OrganizationPhoneDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrganizationPhoneDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ organizationPhone: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrganizationPhoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrganizationPhoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load organizationPhone on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.organizationPhone).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
