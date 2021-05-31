import { IPerson } from 'app/entities/person/person.model';
import { IEvent } from 'app/entities/event/event.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface ITicket {
  id?: number;
  count?: number | null;
  costPerTicket?: number | null;
  person?: IPerson | null;
  event?: IEvent | null;
  transaction?: ITransaction | null;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public count?: number | null,
    public costPerTicket?: number | null,
    public person?: IPerson | null,
    public event?: IEvent | null,
    public transaction?: ITransaction | null
  ) {}
}

export function getTicketIdentifier(ticket: ITicket): number | undefined {
  return ticket.id;
}
