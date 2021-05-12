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

describe('ParishEmail e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ParishEmails', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ParishEmail').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ParishEmail page', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('parishEmail');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ParishEmail page', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ParishEmail');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ParishEmail page', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ParishEmail');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ParishEmail', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ParishEmail');

    cy.get(`[data-cy="email"]`)
      .type('Toby.Wehner@gmail.com', { force: true })
      .invoke('val')
      .should('match', new RegExp('Toby.Wehner@gmail.com'));

    cy.get(`[data-cy="type"]`)
      .type('microchip withdrawal Towels', { force: true })
      .invoke('val')
      .should('match', new RegExp('microchip withdrawal Towels'));

    cy.get(`[data-cy="emailNewsletterSubscription"]`).select('YES');

    cy.get(`[data-cy="emailEventNotificationSubscription"]`).select('NO');

    cy.setFieldSelectToLastOfEntity('parish');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ParishEmail', () => {
    cy.intercept('GET', '/api/parish-emails*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/parish-emails/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('parish-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('parishEmail').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/parish-emails*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('parish-email');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
