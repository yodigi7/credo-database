import { IPerson } from 'app/entities/person/person.model';

export interface IPersonPhone {
  id?: number;
  phoneNumber?: string;
  type?: string | null;
  person?: IPerson | null;
}

export class PersonPhone implements IPersonPhone {
  constructor(public id?: number, public phoneNumber?: string, public type?: string | null, public person?: IPerson | null) {}
}

export function getPersonPhoneIdentifier(personPhone: IPersonPhone): number | undefined {
  return personPhone.id;
}
