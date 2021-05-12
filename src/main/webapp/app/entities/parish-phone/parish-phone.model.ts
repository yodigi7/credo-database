import { IParish } from 'app/entities/parish/parish.model';

export interface IParishPhone {
  id?: number;
  phoneNumber?: string;
  type?: string | null;
  parish?: IParish | null;
}

export class ParishPhone implements IParishPhone {
  constructor(public id?: number, public phoneNumber?: string, public type?: string | null, public parish?: IParish | null) {}
}

export function getParishPhoneIdentifier(parishPhone: IParishPhone): number | undefined {
  return parishPhone.id;
}
