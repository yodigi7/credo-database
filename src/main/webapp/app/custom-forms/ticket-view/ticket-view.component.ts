import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { ITicket, Ticket } from 'app/entities/ticket/ticket.model';

@Component({
  selector: 'jhi-ticket-view',
  templateUrl: './ticket-view.component.html',
  styleUrls: ['./ticket-view.component.css'],
})
export class ticketViewComponent implements OnInit {
  person: IPerson;
  form = this.fb.group({
    tickets: this.fb.array([]),
  });

  constructor(
    private personService: PersonService,
    private fb: FormBuilder,
    private ticketService: TicketService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      this.person.tickets?.forEach(ticket => {
        (this.form.get('tickets') as FormArray).push(this.fb.group(ticket));
      });
    });
  }

  async update(): Promise<void> {
    const tickets = (this.form.get('tickets') as FormArray).controls
      .filter((ticket): boolean => ticket.dirty)
      .map((ticket): ITicket => ticket.value as Ticket);
    for (const ticket of tickets) {
      this.person.tickets = [];
      ticket.person = this.person;
      await this.ticketService.update(ticket).toPromise();
    }
  }
}
