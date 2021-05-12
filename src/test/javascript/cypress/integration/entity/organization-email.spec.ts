import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('OrganizationEmail e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load OrganizationEmails', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('OrganizationEmail').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details OrganizationEmail page', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('organizationEmail');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create OrganizationEmail page', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationEmail');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit OrganizationEmail page', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('OrganizationEmail');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of OrganizationEmail', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationEmail');

    cy.get(`[data-cy="email"]`).type('Tod53@hotmail.com', { force: true }).invoke('val').should('match', new RegExp('Tod53@hotmail.com'));

    cy.get(`[data-cy="type"]`)
      .type('needs-based Refined', { force: true })
      .invoke('val')
      .should('match', new RegExp('needs-based Refined'));

    cy.get(`[data-cy="emailNewsletterSubscription"]`).select('EMPTY');

    cy.get(`[data-cy="emailEventNotificationSubscription"]`).select('EMPTY');

    cy.setFieldSelectToLastOfEntity('organization');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of OrganizationEmail', () => {
    cy.intercept('GET', '/api/organization-emails*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/organization-emails/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('organizationEmail').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/organization-emails*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('organization-email');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
