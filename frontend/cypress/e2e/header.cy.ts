describe('Header Component', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('displays "Log in" and "Sign up" buttons when logged out', () => {
    cy.get('.loginButton').should('be.visible').and('contain', 'Log in');
    cy.get('.signupButton').should('be.visible').and('contain', 'Sign up');
  });

  it('navigates to the correct pages when links are clicked', () => {
    cy.get('.loginButton').click();
    cy.url().should('include', '/login');

    cy.go('back');
    cy.get('.signupButton').click();
    cy.url().should('include', '/signup');
  });

  it('adapts to mobile / logged out verison', () => {
    cy.viewport(375, 667);

    cy.get('#mobileLogo').should('be.visible');
    cy.get('#desktopLogo').should('not.be.visible');
  });

  //TODO: Add test for logged in state: displaying links, navigation and mobile view

  //TODO: Add test for state when there are messages:
});
