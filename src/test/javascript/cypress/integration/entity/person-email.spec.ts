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

describe('PersonEmail e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load PersonEmails', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('PersonEmail').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details PersonEmail page', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('personEmail');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create PersonEmail page', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('PersonEmail');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit PersonEmail page', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('PersonEmail');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of PersonEmail', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('PersonEmail');

    cy.get(`[data-cy="email"]`)
      .type('Stefanie_Barrows@hotmail.com', { force: true })
      .invoke('val')
      .should('match', new RegExp('Stefanie_Barrows@hotmail.com'));

    cy.get(`[data-cy="type"]`).type('Engineer', { force: true }).invoke('val').should('match', new RegExp('Engineer'));

    cy.get(`[data-cy="emailNewsletterSubscription"]`).select('NO');

    cy.get(`[data-cy="emailEventNotificationSubscription"]`).select('EMPTY');

    cy.setFieldSelectToLastOfEntity('person');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of PersonEmail', () => {
    cy.intercept('GET', '/api/person-emails*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/person-emails/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person-email');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('personEmail').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/person-emails*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('person-email');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
