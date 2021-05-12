import * as dayjs from 'dayjs';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IPayment {
  id?: number;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  notes?: string | null;
  tickets?: ITicket | null;
  person?: IPerson | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public amount?: number | null,
    public date?: dayjs.Dayjs | null,
    public notes?: string | null,
    public tickets?: ITicket | null,
    public person?: IPerson | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
