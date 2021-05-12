import { IOrganization } from 'app/entities/organization/organization.model';

export interface IOrganizationPhone {
  id?: number;
  phoneNumber?: string;
  type?: string | null;
  organization?: IOrganization | null;
}

export class OrganizationPhone implements IOrganizationPhone {
  constructor(public id?: number, public phoneNumber?: string, public type?: string | null, public organization?: IOrganization | null) {}
}

export function getOrganizationPhoneIdentifier(organizationPhone: IOrganizationPhone): number | undefined {
  return organizationPhone.id;
}
