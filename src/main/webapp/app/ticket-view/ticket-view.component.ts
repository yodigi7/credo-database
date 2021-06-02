import { Component, OnInit } from '@angular/core';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-ticket-view',
  templateUrl: './ticket-view.component.html',
  styleUrls: ['./ticket-view.component.css'],
})
export class ticketViewComponent implements OnInit {
  person: IPerson;

  constructor(private personService: PersonService) {}

  async ngOnInit(): Promise<void> {
    const res = await this.personService.find(1).toPromise();
    this.person = res.body!;
  }
}
