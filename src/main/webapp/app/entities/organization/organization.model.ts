import { IParish } from 'app/entities/parish/parish.model';
import { IOrganizationNotes } from 'app/entities/organization-notes/organization-notes.model';
import { IOrganizationAddress } from 'app/entities/organization-address/organization-address.model';
import { IOrganizationPhone } from 'app/entities/organization-phone/organization-phone.model';
import { IOrganizationEmail } from 'app/entities/organization-email/organization-email.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IOrganization {
  id?: number;
  name?: string;
  mailingLabel?: string | null;
  parish?: IParish | null;
  notes?: IOrganizationNotes | null;
  addresses?: IOrganizationAddress[] | null;
  phones?: IOrganizationPhone[] | null;
  emails?: IOrganizationEmail[] | null;
  persons?: IPerson[] | null;
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public name?: string,
    public mailingLabel?: string | null,
    public parish?: IParish | null,
    public notes?: IOrganizationNotes | null,
    public addresses?: IOrganizationAddress[] | null,
    public phones?: IOrganizationPhone[] | null,
    public emails?: IOrganizationEmail[] | null,
    public persons?: IPerson[] | null
  ) {}
}

export function getOrganizationIdentifier(organization: IOrganization): number | undefined {
  return organization.id;
}
