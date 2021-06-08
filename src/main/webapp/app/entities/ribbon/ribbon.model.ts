import { IPerson } from 'app/entities/person/person.model';

export interface IRibbon {
  id?: number;
  name?: string | null;
  people?: IPerson[] | null;
}

export class Ribbon implements IRibbon {
  constructor(public id?: number, public name?: string | null, public people?: IPerson[] | null) {}
}

export function getRibbonIdentifier(ribbon: IRibbon): number | undefined {
  return ribbon.id;
}
