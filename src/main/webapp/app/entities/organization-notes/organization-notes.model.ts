import { IOrganization } from 'app/entities/organization/organization.model';

export interface IOrganizationNotes {
  id?: number;
  notes?: string | null;
  organization?: IOrganization | null;
}

export class OrganizationNotes implements IOrganizationNotes {
  constructor(public id?: number, public notes?: string | null, public organization?: IOrganization | null) {}
}

export function getOrganizationNotesIdentifier(organizationNotes: IOrganizationNotes): number | undefined {
  return organizationNotes.id;
}
