import * as dayjs from 'dayjs';
import { IMembershipLevel } from 'app/entities/membership-level/membership-level.model';
import { IParish } from 'app/entities/parish/parish.model';
import { IRelationship } from 'app/entities/relationship/relationship.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { IHouseDetails } from 'app/entities/house-details/house-details.model';
import { IPersonNotes } from 'app/entities/person-notes/person-notes.model';
import { IPersonPhone } from 'app/entities/person-phone/person-phone.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { IPersonEmail } from 'app/entities/person-email/person-email.model';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IPerson {
  id?: number;
  prefix?: string | null;
  preferredName?: string | null;
  firstName?: string | null;
  middleName?: string | null;
  lastName?: string | null;
  suffix?: string | null;
  nameTag?: string | null;
  currentMember?: boolean | null;
  membershipStartDate?: dayjs.Dayjs | null;
  membershipExpirationDate?: dayjs.Dayjs | null;
  isHeadOfHouse?: boolean;
  isDeceased?: boolean;
  spouse?: IPerson | null;
  membershipLevel?: IMembershipLevel | null;
  headOfHouse?: IPerson | null;
  parish?: IParish | null;
  relationship?: IRelationship | null;
  organizations?: IOrganization[] | null;
  houseDetails?: IHouseDetails | null;
  notes?: IPersonNotes | null;
  phones?: IPersonPhone[] | null;
  payments?: IPayment[] | null;
  emails?: IPersonEmail[] | null;
  personsInHouses?: IPerson[] | null;
  tickets?: ITicket[] | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public prefix?: string | null,
    public preferredName?: string | null,
    public firstName?: string | null,
    public middleName?: string | null,
    public lastName?: string | null,
    public suffix?: string | null,
    public nameTag?: string | null,
    public currentMember?: boolean | null,
    public membershipStartDate?: dayjs.Dayjs | null,
    public membershipExpirationDate?: dayjs.Dayjs | null,
    public isHeadOfHouse?: boolean,
    public isDeceased?: boolean,
    public spouse?: IPerson | null,
    public membershipLevel?: IMembershipLevel | null,
    public headOfHouse?: IPerson | null,
    public parish?: IParish | null,
    public relationship?: IRelationship | null,
    public organizations?: IOrganization[] | null,
    public houseDetails?: IHouseDetails | null,
    public notes?: IPersonNotes | null,
    public phones?: IPersonPhone[] | null,
    public payments?: IPayment[] | null,
    public emails?: IPersonEmail[] | null,
    public personsInHouses?: IPerson[] | null,
    public tickets?: ITicket[] | null
  ) {
    this.currentMember = this.currentMember ?? false;
    this.isHeadOfHouse = this.isHeadOfHouse ?? false;
    this.isDeceased = this.isDeceased ?? false;
  }
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
