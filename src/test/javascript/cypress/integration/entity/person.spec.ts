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

describe('Person e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load People', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Person').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Person page', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('person');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Person page', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Person');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Person page', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Person');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Person', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Person');

    cy.get(`[data-cy="prefix"]`).type('Florida Car', { force: true }).invoke('val').should('match', new RegExp('Florida Car'));

    cy.get(`[data-cy="preferredName"]`).type('Investor', { force: true }).invoke('val').should('match', new RegExp('Investor'));

    cy.get(`[data-cy="firstName"]`).type('Audra', { force: true }).invoke('val').should('match', new RegExp('Audra'));

    cy.get(`[data-cy="middleName"]`).type('viral Bike', { force: true }).invoke('val').should('match', new RegExp('viral Bike'));

    cy.get(`[data-cy="lastName"]`).type('Kassulke', { force: true }).invoke('val').should('match', new RegExp('Kassulke'));

    cy.get(`[data-cy="suffix"]`).type('Maine iterate', { force: true }).invoke('val').should('match', new RegExp('Maine iterate'));

    cy.get(`[data-cy="nameTag"]`)
      .type('Bedfordshire CSS SAS', { force: true })
      .invoke('val')
      .should('match', new RegExp('Bedfordshire CSS SAS'));

    cy.get(`[data-cy="currentMember"]`).should('not.be.checked');
    cy.get(`[data-cy="currentMember"]`).click().should('be.checked');

    cy.get(`[data-cy="membershipStartDate"]`).type('2021-05-14').should('have.value', '2021-05-14');

    cy.get(`[data-cy="membershipExpirationDate"]`).type('2021-05-14').should('have.value', '2021-05-14');

    cy.get(`[data-cy="isHeadOfHouse"]`).should('not.be.checked');
    cy.get(`[data-cy="isHeadOfHouse"]`).click().should('be.checked');

    cy.get(`[data-cy="isDeceased"]`).should('not.be.checked');
    cy.get(`[data-cy="isDeceased"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('spouse');

    cy.setFieldSelectToLastOfEntity('membershipLevel');

    cy.setFieldSelectToLastOfEntity('headOfHouse');

    cy.setFieldSelectToLastOfEntity('ribbon');

    cy.setFieldSelectToLastOfEntity('parish');

    cy.setFieldSelectToLastOfEntity('organizations');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/people*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Person', () => {
    cy.intercept('GET', '/api/people*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/people/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('person').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/people*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('person');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
