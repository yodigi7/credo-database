import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { of } from 'rxjs';
import { AddTransactionComponent } from './add-transaction.component';

jest.mock('@angular/router');

describe('Component Tests', () => {
  describe('Add Transaction Component', () => {
    let comp: AddTransactionComponent;
    let fixture: ComponentFixture<AddTransactionComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddTransactionComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AddTransactionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddTransactionComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Person query and add missing value', () => {
        const person: IPerson = { id: 456 };

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.addTransaction).not.toBeFalsy();
        expect(comp.addTransaction.get('person')?.get('id')).not.toBeFalsy();
      });
    });
  });
});
