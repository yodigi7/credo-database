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

describe('EventPerk e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load EventPerks', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('EventPerk').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details EventPerk page', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('eventPerk');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create EventPerk page', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('EventPerk');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit EventPerk page', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('EventPerk');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of EventPerk', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('EventPerk');

    cy.get(`[data-cy="name"]`)
      .type('orchestrate out-of-the-box Berkshire', { force: true })
      .invoke('val')
      .should('match', new RegExp('orchestrate out-of-the-box Berkshire'));

    cy.get(`[data-cy="minimumPrice"]`).type('53899').should('have.value', '53899');

    cy.setFieldSelectToLastOfEntity('event');

    cy.setFieldSelectToLastOfEntity('person');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of EventPerk', () => {
    cy.intercept('GET', '/api/event-perks*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/event-perks/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('event-perk');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('eventPerk').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/event-perks*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('event-perk');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
