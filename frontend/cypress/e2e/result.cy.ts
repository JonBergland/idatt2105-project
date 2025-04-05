describe('ResultView', () => {
  beforeEach(() => {
    cy.visit('/result');
  });

  it('displays the filter and toggles its visibility', () => {
    cy.get('.filter-container').should('be.visible');

    cy.viewport(500, 800);
    cy.get('.filter-container').should('not.exist');

    cy.get('.filter-toggle-button').click();
    cy.get('.filter-container').should('be.visible');
  });

  //TODO: Add more tests when fully implemented
});
