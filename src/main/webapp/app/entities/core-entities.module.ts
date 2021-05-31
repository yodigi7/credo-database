import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';

import { EventModule } from '../entities/event/event.module';
import { HouseAddressModule } from '../entities/house-address/house-address.module';
import { HouseDetailsModule } from '../entities/house-details/house-details.module';
import { MembershipLevelModule } from '../entities/membership-level/membership-level.module';
import { OrganizationModule } from '../entities/organization/organization.module';
import { OrganizationAddressModule } from '../entities/organization-address/organization-address.module';
import { OrganizationEmailModule } from '../entities/organization-email/organization-email.module';
import { OrganizationNotesModule } from '../entities/organization-notes/organization-notes.module';
import { OrganizationPhoneModule } from '../entities/organization-phone/organization-phone.module';
import { ParishModule } from '../entities/parish/parish.module';
import { ParishEmailModule } from '../entities/parish-email/parish-email.module';
import { ParishPhoneModule } from '../entities/parish-phone/parish-phone.module';
import { PersonModule } from '../entities/person/person.module';
import { PersonEmailModule } from '../entities/person-email/person-email.module';
import { PersonNotesModule } from '../entities/person-notes/person-notes.module';
import { PersonPhoneModule } from '../entities/person-phone/person-phone.module';
import { TicketModule } from '../entities/ticket/ticket.module';

@NgModule({
  imports: [
    SharedModule,
    ParishModule,
    EventModule,
    HouseAddressModule,
    HouseDetailsModule,
    MembershipLevelModule,
    OrganizationAddressModule,
    OrganizationModule,
    OrganizationEmailModule,
    OrganizationNotesModule,
    OrganizationPhoneModule,
    ParishEmailModule,
    ParishPhoneModule,
    PersonModule,
    PersonEmailModule,
    PersonNotesModule,
    PersonPhoneModule,
    TicketModule,
  ],
  exports: [
    SharedModule,
    ParishModule,
    EventModule,
    HouseAddressModule,
    HouseDetailsModule,
    MembershipLevelModule,
    OrganizationAddressModule,
    OrganizationModule,
    OrganizationEmailModule,
    OrganizationNotesModule,
    OrganizationPhoneModule,
    ParishEmailModule,
    ParishPhoneModule,
    PersonModule,
    PersonEmailModule,
    PersonNotesModule,
    PersonPhoneModule,
    TicketModule,
  ],
})
export class CoreEntitiesModule {}
