import { IPerson } from 'app/entities/person/person.model';

export interface IPersonNotes {
  id?: number;
  notes?: string | null;
  person?: IPerson | null;
}

export class PersonNotes implements IPersonNotes {
  constructor(public id?: number, public notes?: string | null, public person?: IPerson | null) {}
}

export function getPersonNotesIdentifier(personNotes: IPersonNotes): number | undefined {
  return personNotes.id;
}
