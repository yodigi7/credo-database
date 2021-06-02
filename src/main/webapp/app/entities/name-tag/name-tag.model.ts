import { ITicket } from 'app/entities/ticket/ticket.model';

export interface INameTag {
  id?: number;
  nameTag?: string | null;
  ticket?: ITicket | null;
}

export class NameTag implements INameTag {
  constructor(public id?: number, public nameTag?: string | null, public ticket?: ITicket | null) {}
}

export function getNameTagIdentifier(nameTag: INameTag): number | undefined {
  return nameTag.id;
}
