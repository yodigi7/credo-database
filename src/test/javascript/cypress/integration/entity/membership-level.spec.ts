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

describe('MembershipLevel e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MembershipLevels', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MembershipLevel').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MembershipLevel page', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('membershipLevel');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MembershipLevel page', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MembershipLevel');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MembershipLevel page', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MembershipLevel');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of MembershipLevel', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MembershipLevel');

    cy.get(`[data-cy="level"]`).type('technologies', { force: true }).invoke('val').should('match', new RegExp('technologies'));

    cy.get(`[data-cy="cost"]`).type('88767').should('have.value', '88767');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of MembershipLevel', () => {
    cy.intercept('GET', '/api/membership-levels*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/membership-levels/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-level');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('membershipLevel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/membership-levels*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('membership-level');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
