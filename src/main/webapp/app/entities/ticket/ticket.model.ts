import { IPerson } from 'app/entities/person/person.model';
import { IEvent } from 'app/entities/event/event.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { INameTag } from 'app/entities/name-tag/name-tag.model';

export interface ITicket {
  id?: number;
  count?: number | null;
  costPerTicket?: number | null;
  pickedUp?: boolean | null;
  person?: IPerson | null;
  event?: IEvent | null;
  transaction?: ITransaction | null;
  nameTags?: INameTag[] | null;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public count?: number | null,
    public costPerTicket?: number | null,
    public pickedUp?: boolean | null,
    public person?: IPerson | null,
    public event?: IEvent | null,
    public transaction?: ITransaction | null,
    public nameTags?: INameTag[] | null
  ) {
    this.pickedUp = this.pickedUp ?? false;
  }
}

export function getTicketIdentifier(ticket: ITicket): number | undefined {
  return ticket.id;
}
