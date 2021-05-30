import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Authority } from 'app/config/authority.constants';
import { User } from '../user-management.model';

import { UserManagementDetailComponent } from './user-management-detail.component';

describe('Component Tests', () => {
  describe('User Management Detail Component', () => {
    let comp: UserManagementDetailComponent;
    let fixture: ComponentFixture<UserManagementDetailComponent>;

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          declarations: [UserManagementDetailComponent],
          providers: [
            {
              provide: ActivatedRoute,
              useValue: {
                data: of({ user: new User(123, 'user', true, [Authority.SUPERVISOR], 'admin') }),
              },
            },
          ],
        })
          .overrideTemplate(UserManagementDetailComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(UserManagementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.user).toEqual(
          jasmine.objectContaining({
            id: 123,
            login: 'user',
            activated: true,
            authorities: [Authority.SUPERVISOR],
            createdBy: 'admin',
          })
        );
      });
    });
  });
});
