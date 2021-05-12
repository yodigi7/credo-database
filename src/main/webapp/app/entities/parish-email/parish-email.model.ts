import { IParish } from 'app/entities/parish/parish.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';

export interface IParishEmail {
  id?: number;
  email?: string;
  type?: string | null;
  emailNewsletterSubscription?: YesNoEmpty | null;
  emailEventNotificationSubscription?: YesNoEmpty | null;
  parish?: IParish | null;
}

export class ParishEmail implements IParishEmail {
  constructor(
    public id?: number,
    public email?: string,
    public type?: string | null,
    public emailNewsletterSubscription?: YesNoEmpty | null,
    public emailEventNotificationSubscription?: YesNoEmpty | null,
    public parish?: IParish | null
  ) {}
}

export function getParishEmailIdentifier(parishEmail: IParishEmail): number | undefined {
  return parishEmail.id;
}
