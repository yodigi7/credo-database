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

describe('OrganizationAddress e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load OrganizationAddresses', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('OrganizationAddress').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details OrganizationAddress page', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('organizationAddress');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create OrganizationAddress page', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationAddress');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit OrganizationAddress page', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('OrganizationAddress');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of OrganizationAddress', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationAddress');

    cy.get(`[data-cy="streetAddress"]`)
      .type('Supervisor microchip Borders', { force: true })
      .invoke('val')
      .should('match', new RegExp('Supervisor microchip Borders'));

    cy.get(`[data-cy="city"]`).type('Temple', { force: true }).invoke('val').should('match', new RegExp('Temple'));

    cy.get(`[data-cy="state"]`).type('customized', { force: true }).invoke('val').should('match', new RegExp('customized'));

    cy.get(`[data-cy="zipcode"]`)
      .type('Business-focused Greenland', { force: true })
      .invoke('val')
      .should('match', new RegExp('Business-focused Greenland'));

    cy.setFieldSelectToLastOfEntity('organization');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of OrganizationAddress', () => {
    cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/organization-addresses/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('organizationAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/organization-addresses*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('organization-address');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
