import { IOrganization } from 'app/entities/organization/organization.model';
import { IParishPhone } from 'app/entities/parish-phone/parish-phone.model';
import { IPerson } from 'app/entities/person/person.model';
import { IParishEmail } from 'app/entities/parish-email/parish-email.model';

export interface IParish {
  id?: number;
  name?: string;
  organizations?: IOrganization[] | null;
  phones?: IParishPhone[] | null;
  people?: IPerson[] | null;
  emails?: IParishEmail[] | null;
}

export class Parish implements IParish {
  constructor(
    public id?: number,
    public name?: string,
    public organizations?: IOrganization[] | null,
    public phones?: IParishPhone[] | null,
    public people?: IPerson[] | null,
    public emails?: IParishEmail[] | null
  ) {}
}

export function getParishIdentifier(parish: IParish): number | undefined {
  return parish.id;
}
