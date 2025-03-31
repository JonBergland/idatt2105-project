describe('Login-page test', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('visits the login page and checks for elements', () => {
    cy.contains('h1', 'Log In');
    cy.contains('button', 'Sign up');
  });

  it('checks that the LogInForm component is present', () => {
    cy.get('#login-form').should('exist');
    cy.get('#login-form').within(() => {
      cy.get('input#email').should('exist');
      cy.get('input#password').should('exist');
      cy.get('input#login-button').should('exist');
    });
  });

  it('checks that the login button is disabled when fields are empty', () => {
    cy.get('#login-button').should('be.disabled');
  });

  it('checks that the login button is disabled when only one field is filled', () => {
    cy.get('input#email').type('test@example.com');
    cy.get('#login-button').should('be.disabled');

    cy.get('input#email').clear();
    cy.get('input#password').type('password123');
    cy.get('#login-button').should('be.disabled');
  });

  it('checks that the login button is enabled when both fields are filled', () => {
    cy.get('input#email').type('test@example.com');
    cy.get('input#password').type('password123');
    cy.get('#login-button').should('not.be.disabled');
  });

  // TODO: Add logic for checking for error when implemented in LogInForm
  // it('checks for error message on invalid form submission', () => {
  //   cy.get('#login-button').click();
  //   cy.get('#login-status-label').should('contain.text', 'Log in failed:');
  // });
});
