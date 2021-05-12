import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganizationAddressDetailComponent } from './organization-address-detail.component';

describe('Component Tests', () => {
  describe('OrganizationAddress Management Detail Component', () => {
    let comp: OrganizationAddressDetailComponent;
    let fixture: ComponentFixture<OrganizationAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrganizationAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ organizationAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrganizationAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrganizationAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load organizationAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.organizationAddress).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
