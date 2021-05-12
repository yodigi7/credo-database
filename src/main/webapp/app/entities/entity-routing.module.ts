import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        data: { pageTitle: 'People' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'relationship',
        data: { pageTitle: 'Relationships' },
        loadChildren: () => import('./relationship/relationship.module').then(m => m.RelationshipModule),
      },
      {
        path: 'person-notes',
        data: { pageTitle: 'PersonNotes' },
        loadChildren: () => import('./person-notes/person-notes.module').then(m => m.PersonNotesModule),
      },
      {
        path: 'organization',
        data: { pageTitle: 'Organizations' },
        loadChildren: () => import('./organization/organization.module').then(m => m.OrganizationModule),
      },
      {
        path: 'organization-notes',
        data: { pageTitle: 'OrganizationNotes' },
        loadChildren: () => import('./organization-notes/organization-notes.module').then(m => m.OrganizationNotesModule),
      },
      {
        path: 'person-phone',
        data: { pageTitle: 'PersonPhones' },
        loadChildren: () => import('./person-phone/person-phone.module').then(m => m.PersonPhoneModule),
      },
      {
        path: 'organization-phone',
        data: { pageTitle: 'OrganizationPhones' },
        loadChildren: () => import('./organization-phone/organization-phone.module').then(m => m.OrganizationPhoneModule),
      },
      {
        path: 'parish-phone',
        data: { pageTitle: 'ParishPhones' },
        loadChildren: () => import('./parish-phone/parish-phone.module').then(m => m.ParishPhoneModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'Payments' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'parish',
        data: { pageTitle: 'Parishes' },
        loadChildren: () => import('./parish/parish.module').then(m => m.ParishModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'Events' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'ticket',
        data: { pageTitle: 'Tickets' },
        loadChildren: () => import('./ticket/ticket.module').then(m => m.TicketModule),
      },
      {
        path: 'person-email',
        data: { pageTitle: 'PersonEmails' },
        loadChildren: () => import('./person-email/person-email.module').then(m => m.PersonEmailModule),
      },
      {
        path: 'organization-email',
        data: { pageTitle: 'OrganizationEmails' },
        loadChildren: () => import('./organization-email/organization-email.module').then(m => m.OrganizationEmailModule),
      },
      {
        path: 'parish-email',
        data: { pageTitle: 'ParishEmails' },
        loadChildren: () => import('./parish-email/parish-email.module').then(m => m.ParishEmailModule),
      },
      {
        path: 'house-details',
        data: { pageTitle: 'HouseDetails' },
        loadChildren: () => import('./house-details/house-details.module').then(m => m.HouseDetailsModule),
      },
      {
        path: 'house-address',
        data: { pageTitle: 'HouseAddresses' },
        loadChildren: () => import('./house-address/house-address.module').then(m => m.HouseAddressModule),
      },
      {
        path: 'organization-address',
        data: { pageTitle: 'OrganizationAddresses' },
        loadChildren: () => import('./organization-address/organization-address.module').then(m => m.OrganizationAddressModule),
      },
      {
        path: 'membership-level',
        data: { pageTitle: 'MembershipLevels' },
        loadChildren: () => import('./membership-level/membership-level.module').then(m => m.MembershipLevelModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
