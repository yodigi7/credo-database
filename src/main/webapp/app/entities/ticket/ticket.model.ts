import { IPerson } from 'app/entities/person/person.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { IEvent } from 'app/entities/event/event.model';

export interface ITicket {
  id?: number;
  count?: number | null;
  person?: IPerson | null;
  payments?: IPayment[] | null;
  events?: IEvent[] | null;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public count?: number | null,
    public person?: IPerson | null,
    public payments?: IPayment[] | null,
    public events?: IEvent[] | null
  ) {}
}

export function getTicketIdentifier(ticket: ITicket): number | undefined {
  return ticket.id;
}
