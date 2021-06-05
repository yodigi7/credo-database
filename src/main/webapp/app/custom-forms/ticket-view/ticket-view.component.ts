import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { ITicket, Ticket } from 'app/entities/ticket/ticket.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';

@Component({
  selector: 'jhi-ticket-view',
  templateUrl: './ticket-view.component.html',
  styleUrls: ['./ticket-view.component.css'],
})
export class ticketViewComponent implements OnInit {
  person: IPerson;
  form = this.fb.group({
    transactions: this.fb.array([]),
  });

  constructor(
    private personService: PersonService,
    private fb: FormBuilder,
    private ticketService: TicketService,
    private transactionService: TransactionService,
    private activatedRoute: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      // TODO: don't make calls just use return from person when fixed
      this.person.transactions
        ?.filter(transaction => transaction.id)
        .forEach(transaction =>
          this.transactionService.find(transaction.id!).subscribe(res => {
            const transactionRes = res.body!;
            (this.form.get('transactions') as FormArray).push(this.fb.group(transactionRes));
            console.log(this.form.controls);
          })
        );
    });
  }

  async update(): Promise<void> {
    const tickets = (this.form.get('transactions') as FormArray).controls
      .filter((ticket): boolean => ticket.dirty)
      .map((ticket): ITicket => ticket.value as Ticket);
    for (const ticket of tickets) {
      this.person.tickets = [];
      ticket.person = this.person;
      await this.ticketService.update(ticket).toPromise();
    }
    this.location.back();
  }
}
