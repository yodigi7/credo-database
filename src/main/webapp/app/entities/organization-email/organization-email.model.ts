import { IOrganization } from 'app/entities/organization/organization.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';

export interface IOrganizationEmail {
  id?: number;
  email?: string;
  type?: string | null;
  emailNewsletterSubscription?: YesNoEmpty | null;
  emailEventNotificationSubscription?: YesNoEmpty | null;
  organization?: IOrganization | null;
}

export class OrganizationEmail implements IOrganizationEmail {
  constructor(
    public id?: number,
    public email?: string,
    public type?: string | null,
    public emailNewsletterSubscription?: YesNoEmpty | null,
    public emailEventNotificationSubscription?: YesNoEmpty | null,
    public organization?: IOrganization | null
  ) {}
}

export function getOrganizationEmailIdentifier(organizationEmail: IOrganizationEmail): number | undefined {
  return organizationEmail.id;
}
