import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        data: { pageTitle: 'People', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'person-notes',
        data: { pageTitle: 'PersonNotes', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./person-notes/person-notes.module').then(m => m.PersonNotesModule),
      },
      {
        path: 'organization',
        data: { pageTitle: 'Organizations', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./organization/organization.module').then(m => m.OrganizationModule),
      },
      {
        path: 'organization-notes',
        data: { pageTitle: 'OrganizationNotes', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./organization-notes/organization-notes.module').then(m => m.OrganizationNotesModule),
      },
      {
        path: 'person-phone',
        data: { pageTitle: 'PersonPhones', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./person-phone/person-phone.module').then(m => m.PersonPhoneModule),
      },
      {
        path: 'organization-phone',
        data: { pageTitle: 'OrganizationPhones', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./organization-phone/organization-phone.module').then(m => m.OrganizationPhoneModule),
      },
      {
        path: 'parish-phone',
        data: { pageTitle: 'ParishPhones', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./parish-phone/parish-phone.module').then(m => m.ParishPhoneModule),
      },
      {
        path: 'parish',
        data: { pageTitle: 'Parishes', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./parish/parish.module').then(m => m.ParishModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'Events', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'ticket',
        data: { pageTitle: 'Tickets', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./ticket/ticket.module').then(m => m.TicketModule),
      },
      {
        path: 'person-email',
        data: { pageTitle: 'PersonEmails', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./person-email/person-email.module').then(m => m.PersonEmailModule),
      },
      {
        path: 'organization-email',
        data: { pageTitle: 'OrganizationEmails', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./organization-email/organization-email.module').then(m => m.OrganizationEmailModule),
      },
      {
        path: 'parish-email',
        data: { pageTitle: 'ParishEmails', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./parish-email/parish-email.module').then(m => m.ParishEmailModule),
      },
      {
        path: 'house-details',
        data: { pageTitle: 'HouseDetails', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./house-details/house-details.module').then(m => m.HouseDetailsModule),
      },
      {
        path: 'house-address',
        data: { pageTitle: 'HouseAddresses', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./house-address/house-address.module').then(m => m.HouseAddressModule),
      },
      {
        path: 'organization-address',
        data: { pageTitle: 'OrganizationAddresses', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./organization-address/organization-address.module').then(m => m.OrganizationAddressModule),
      },
      {
        path: 'membership-level',
        data: { pageTitle: 'MembershipLevels', authorities: [Authority.ADMIN] },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./membership-level/membership-level.module').then(m => m.MembershipLevelModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'Transactions' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'name-tag',
        data: { pageTitle: 'NameTags' },
        loadChildren: () => import('./name-tag/name-tag.module').then(m => m.NameTagModule),
      },
      {
        path: 'ribbon',
        data: { pageTitle: 'Ribbons' },
        loadChildren: () => import('./ribbon/ribbon.module').then(m => m.RibbonModule),
      },
      {
        path: 'event-perk',
        data: { pageTitle: 'EventPerks' },
        loadChildren: () => import('./event-perk/event-perk.module').then(m => m.EventPerkModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
