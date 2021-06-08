import { IEvent } from 'app/entities/event/event.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IEventPerk {
  id?: number;
  name?: string | null;
  minimumPrice?: number | null;
  event?: IEvent | null;
  person?: IPerson | null;
}

export class EventPerk implements IEventPerk {
  constructor(
    public id?: number,
    public name?: string | null,
    public minimumPrice?: number | null,
    public event?: IEvent | null,
    public person?: IPerson | null
  ) {}
}

export function getEventPerkIdentifier(eventPerk: IEventPerk): number | undefined {
  return eventPerk.id;
}
