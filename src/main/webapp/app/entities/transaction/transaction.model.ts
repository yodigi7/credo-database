import * as dayjs from 'dayjs';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { IPerson } from 'app/entities/person/person.model';
import { IEvent } from 'app/entities/event/event.model';

export interface ITransaction {
  id?: number;
  totalAmount?: number | null;
  date?: dayjs.Dayjs | null;
  genericSubItemsPurchased?: string | null;
  costSubItemsPurchased?: number | null;
  numberOfMemberships?: number | null;
  donation?: number | null;
  eventDonation?: number | null;
  notes?: string | null;
  tickets?: ITicket | null;
  membershipLevel?: IMembershipLevel | null;
  person?: IPerson | null;
  event?: IEvent | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public totalAmount?: number | null,
    public date?: dayjs.Dayjs | null,
    public genericSubItemsPurchased?: string | null,
    public costSubItemsPurchased?: number | null,
    public numberOfMemberships?: number | null,
    public donation?: number | null,
    public eventDonation?: number | null,
    public notes?: string | null,
    public tickets?: ITicket | null,
    public membershipLevel?: IMembershipLevel | null,
    public person?: IPerson | null,
    public event?: IEvent | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
