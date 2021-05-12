import { IOrganization } from 'app/entities/organization/organization.model';

export interface IOrganizationAddress {
  id?: number;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  zipcode?: string | null;
  organization?: IOrganization | null;
}

export class OrganizationAddress implements IOrganizationAddress {
  constructor(
    public id?: number,
    public streetAddress?: string | null,
    public city?: string | null,
    public state?: string | null,
    public zipcode?: string | null,
    public organization?: IOrganization | null
  ) {}
}

export function getOrganizationAddressIdentifier(organizationAddress: IOrganizationAddress): number | undefined {
  return organizationAddress.id;
}
