import { IPerson } from 'app/entities/person/person.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';

export interface IPersonEmail {
  id?: number;
  email?: string;
  type?: string | null;
  emailNewsletterSubscription?: YesNoEmpty | null;
  emailEventNotificationSubscription?: YesNoEmpty | null;
  person?: IPerson | null;
}

export class PersonEmail implements IPersonEmail {
  constructor(
    public id?: number,
    public email?: string,
    public type?: string | null,
    public emailNewsletterSubscription?: YesNoEmpty | null,
    public emailEventNotificationSubscription?: YesNoEmpty | null,
    public person?: IPerson | null
  ) {}
}

export function getPersonEmailIdentifier(personEmail: IPersonEmail): number | undefined {
  return personEmail.id;
}
