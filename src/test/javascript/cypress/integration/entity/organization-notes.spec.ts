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

describe('OrganizationNotes e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load OrganizationNotes', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('OrganizationNotes').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details OrganizationNotes page', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('organizationNotes');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create OrganizationNotes page', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationNotes');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit OrganizationNotes page', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('OrganizationNotes');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of OrganizationNotes', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('OrganizationNotes');

    cy.get(`[data-cy="notes"]`)
      .type('Buckinghamshire feed', { force: true })
      .invoke('val')
      .should('match', new RegExp('Buckinghamshire feed'));

    cy.setFieldSelectToLastOfEntity('organization');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of OrganizationNotes', () => {
    cy.intercept('GET', '/api/organization-notes*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/organization-notes/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-notes');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('organizationNotes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/organization-notes*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('organization-notes');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
