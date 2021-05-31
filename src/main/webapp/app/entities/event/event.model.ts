import * as dayjs from 'dayjs';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IEvent {
  id?: number;
  name?: string;
  date?: dayjs.Dayjs | null;
  tickets?: ITicket[] | null;
}

export class Event implements IEvent {
  constructor(public id?: number, public name?: string, public date?: dayjs.Dayjs | null, public tickets?: ITicket[] | null) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
