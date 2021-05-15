import { IHouseDetails } from 'app/entities/house-details/house-details.model';
import { YesNoEmpty } from 'app/entities/enumerations/yes-no-empty.model';

export interface IHouseAddress {
  id?: number;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  zipcode?: string | null;
  type?: string | null;
  mailNewsletterSubscription?: YesNoEmpty | null;
  mailEventNotificationSubscription?: YesNoEmpty | null;
  houseDetails?: IHouseDetails | null;
}

export class HouseAddress implements IHouseAddress {
  constructor(
    public id?: number,
    public streetAddress?: string | null,
    public city?: string | null,
    public state?: string | null,
    public zipcode?: string | null,
    public type?: string | null,
    public mailNewsletterSubscription?: YesNoEmpty | null,
    public mailEventNotificationSubscription?: YesNoEmpty | null,
    public houseDetails?: IHouseDetails | null
  ) {
    this.mailEventNotificationSubscription = this.mailEventNotificationSubscription ?? YesNoEmpty.EMPTY;
    this.mailNewsletterSubscription = this.mailNewsletterSubscription ?? YesNoEmpty.EMPTY;
  }
}

export function getHouseAddressIdentifier(houseAddress: IHouseAddress): number | undefined {
  return houseAddress.id;
}
