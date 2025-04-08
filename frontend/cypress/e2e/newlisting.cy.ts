describe('New Listing Page', () => {
  // Mock user data for testing
  const mockUser = {
    userID: 1,
    name: 'John',
    surname: 'Doe',
    email: 'john.doe@example.com',
    phoneNumber: 12345678,
    countryCode: 47,
    city: 'Oslo',
    role: 'ROLE_USER',
    latitude: 59.9139,
    longitude: 10.7522,
    postalCode: 150,
    address: '123 Main St'
  };


  beforeEach(() => {
    cy.visit('/listing');
    // Mock initial auth state check
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 200,
      body: mockUser
    }).as('getUserInfo');

    // Use the same wildcard pattern for categories
    cy.intercept('GET', '**/api/store/category', {
      statusCode: 200,
      // Match the expected response format
      body: { categories: ['Electronics', 'Clothing', 'Books', 'Furniture'] }
    }).as('getCategories');

    cy.wait('@getUserInfo');
  });

  it('redirects unauthenticated users to login page', () => {
    // Override the auth check to return unauthorized
    cy.intercept('GET', '/api/user/info', {
      statusCode: 401
    }).as('unauthorizedUser');

    cy.visit('/listing');
    cy.url().should('include', '/login');
  });

  it('displays the form with all required fields', () => {
    cy.get('h1').should('contain', 'Create New Listing');
    cy.get('#name').should('exist');
    cy.get('#description').should('exist');
    cy.get('#price').should('exist');
    cy.get('#category').should('exist');
    cy.get('#submit-button').should('exist').and('be.disabled');
  });

  it('validates the name field', () => {
    cy.wait('@getCategories');
    // Test empty name validation
    cy.get('#name').focus().blur();
    cy.get('.error-msg').should('contain', 'Name must be at least 3 characters');

    // Test too short name validation
    cy.get('#name').type('ab').blur();
    cy.get('.error-msg').should('contain', 'Name must be at least 3 characters');

    // Test valid name
    cy.get('#name').clear().type('Valid Item Name').blur();
    cy.get('.error-msg').should('not.exist');
  });

  it('validates the description field', () => {
    // Test empty description validation
    cy.get('#description').focus().blur();
    cy.get('.error-msg').should('contain', 'Description must be at least 3 characters');

    // Test valid description
    cy.get('#description').type('This is a valid description for the item').blur();
    cy.get('.error-msg').should('not.exist');
  });

  it('enables submit button when form is valid', () => {
    // Fill all form fields with valid data
    cy.get('#name').type('Test Item');
    cy.get('#description').type('This is a test item description');
    cy.get('#price').type('1000');

    // Select a category - need to handle the ToggleGroup component
    cy.contains('Electronics').click();

    // Check if button is enabled
    cy.get('#submit-button').should('not.be.disabled');
  });

  it('shows loading state during submission', () => {
    // Mock a delayed response to see loading state
    cy.intercept('POST', '/api/user/item', {
      statusCode: 200,
      delay: 500,
      body: { success: true }
    }).as('createItem');

    // Fill all form fields
    cy.get('#name').type('Test Item');
    cy.get('#description').type('This is a test item description');
    cy.get('#price').type('1000');
    cy.contains('Electronics').click();

    // Submit the form
    cy.get('#submit-button').click();

    // Check loading indicator
    cy.get('.loading-indicator').should('be.visible')
      .and('contain', 'Creating your listing');

    // Wait for submission to complete
    cy.wait('@createItem');
    cy.get('.loading-indicator').should('not.exist');
  });

  it('shows success message and redirects after successful submission', () => {
    // Mock successful item creation
    cy.intercept('POST', '/api/user/item', {
      statusCode: 200,
      body: { success: true }
    }).as('createItem');

    // Fill all form fields
    cy.get('#name').type('Test Item');
    cy.get('#description').type('This is a test item description');
    cy.get('#price').type('1000');
    cy.contains('Electronics').click();

    // Submit the form
    cy.get('#submit-button').click();

    // Check for success message
    cy.wait('@createItem');
    cy.get('.success-message').should('be.visible')
      .and('contain', 'Item listed successfully');

    // Verify redirection after timeout
    cy.url().should('include', '/');
  });

  it('shows error message when submission fails', () => {
    // Mock failed item creation
    cy.intercept('POST', '/api/user/item', {
      statusCode: 500,
      body: { message: 'Server error' }
    }).as('createItemError');

    // Fill all form fields
    cy.get('#name').type('Test Item');
    cy.get('#description').type('This is a test item description');
    cy.get('#price').type('1000');
    cy.contains('Electronics').click();

    // Submit the form
    cy.get('#submit-button').click();

    // Check for error message
    cy.wait('@createItemError');
    cy.get('.error-message').should('be.visible')
      .and('contain', 'Failed to post item');
  });
});
