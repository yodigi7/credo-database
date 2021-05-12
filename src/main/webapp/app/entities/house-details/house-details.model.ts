import { IPerson } from 'app/entities/person/person.model';
import { IHouseAddress } from 'app/entities/house-address/house-address.model';

export interface IHouseDetails {
  id?: number;
  mailingLabel?: string | null;
  headOfHouse?: IPerson | null;
  addresses?: IHouseAddress[] | null;
}

export class HouseDetails implements IHouseDetails {
  constructor(
    public id?: number,
    public mailingLabel?: string | null,
    public headOfHouse?: IPerson | null,
    public addresses?: IHouseAddress[] | null
  ) {}
}

export function getHouseDetailsIdentifier(houseDetails: IHouseDetails): number | undefined {
  return houseDetails.id;
}
