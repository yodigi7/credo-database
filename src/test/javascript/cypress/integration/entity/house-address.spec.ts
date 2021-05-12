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

describe('HouseAddress e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load HouseAddresses', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('HouseAddress').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details HouseAddress page', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('houseAddress');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create HouseAddress page', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('HouseAddress');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit HouseAddress page', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('HouseAddress');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of HouseAddress', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('HouseAddress');

    cy.get(`[data-cy="streetAddress"]`).type('synergize', { force: true }).invoke('val').should('match', new RegExp('synergize'));

    cy.get(`[data-cy="city"]`).type('Nadermouth', { force: true }).invoke('val').should('match', new RegExp('Nadermouth'));

    cy.get(`[data-cy="state"]`).type('Savings middleware', { force: true }).invoke('val').should('match', new RegExp('Savings middleware'));

    cy.get(`[data-cy="zipcode"]`).type('haptic', { force: true }).invoke('val').should('match', new RegExp('haptic'));

    cy.get(`[data-cy="type"]`).type('expedite', { force: true }).invoke('val').should('match', new RegExp('expedite'));

    cy.get(`[data-cy="mailNewsletterSubscription"]`).select('YES');

    cy.get(`[data-cy="mailEventNotificationSubscription"]`).select('YES');

    cy.setFieldSelectToLastOfEntity('houseDetails');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of HouseAddress', () => {
    cy.intercept('GET', '/api/house-addresses*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/house-addresses/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('house-address');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('houseAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/house-addresses*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('house-address');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
