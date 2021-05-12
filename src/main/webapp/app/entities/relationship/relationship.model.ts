import { IPerson } from 'app/entities/person/person.model';

export interface IRelationship {
  id?: number;
  relationship?: string | null;
  people?: IPerson[] | null;
}

export class Relationship implements IRelationship {
  constructor(public id?: number, public relationship?: string | null, public people?: IPerson[] | null) {}
}

export function getRelationshipIdentifier(relationship: IRelationship): number | undefined {
  return relationship.id;
}
