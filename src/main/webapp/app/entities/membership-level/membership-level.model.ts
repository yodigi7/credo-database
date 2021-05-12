import { IPerson } from 'app/entities/person/person.model';

export interface IMembershipLevel {
  id?: number;
  level?: string;
  people?: IPerson[] | null;
}

export class MembershipLevel implements IMembershipLevel {
  constructor(public id?: number, public level?: string, public people?: IPerson[] | null) {}
}

export function getMembershipLevelIdentifier(membershipLevel: IMembershipLevel): number | undefined {
  return membershipLevel.id;
}
