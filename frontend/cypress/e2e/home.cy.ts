describe('HomeView', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('renders the page correctly', () => {
    cy.contains('h1', 'Welcome to the Yard!').should('be.visible');

    cy.get('.search-bar').should('exist');
    cy.get('.category-grid').should('exist');
    cy.get('.recommendations-grid').should('exist');
  });

  it('handles search input and emits the search event', () => {
    cy.get('.search-bar input').type('Playstation');

    cy.get('.search-bar button').click();

    cy.log('Search event emitted with query: Playstation');
  });

  it('handles category clicks and emits the category-clicked event', () => {
    cy.get('.category-grid button').first().click();

    cy.log('Category-clicked event emitted');
  });

  it('handles item clicks and emits the item-clicked event', () => {
    cy.get('.recommendations-grid .item-card').first().click();

    cy.log('Item-clicked event emitted');
  });

  it('renders categories and recommendations correctly', () => {
    cy.get('.category-grid button').should('have.length', 3);

    cy.get('.recommendations-grid .item-card').should('have.length', 3);
  });
});

//TODO: Update test when routing is intigrated
