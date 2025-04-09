describe('Profile Page', () => {
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

  // Mock user items data
  const mockItems = [
    {
      itemID: 1,
      name: 'Test Item 1',
      price: 1000,
      category: 'Electronics',
      seller: 'John Doe',
      description: 'This is a test item',
      published: '2023-04-01',
      state: 'available'
    },
    {
      itemID: 2,
      name: 'Test Item 2',
      price: 2000,
      category: 'Furniture',
      seller: 'John Doe',
      description: 'This is another test item',
      published: '2023-04-02',
      state: 'available'
    }
  ];

  beforeEach(() => {
    // Intercept auth check
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 200,
      body: mockUser
    }).as('getUserInfo');

    // Intercept user items
    cy.intercept('GET', '**/api/user/item', {
      statusCode: 200,
      body: { items: mockItems }
    }).as('getUserItems');

    // Visit the profile page
    cy.visit('/profile');

    // Wait for the initial API requests to complete
    cy.wait('@getUserInfo');
    cy.wait('@getUserItems');
  });

  it('displays user information correctly', () => {
    // Check that the user's name is displayed
    cy.contains(`${mockUser.name} ${mockUser.surname}`).should('be.visible');
    cy.contains(`First name: ${mockUser.name}`).should('be.visible');
    cy.contains(`Last name: ${mockUser.surname}`).should('be.visible');
    cy.contains(`Email: ${mockUser.email}`).should('be.visible');
    cy.contains(`Phone number: +${mockUser.countryCode} ${mockUser.phoneNumber}`).should('be.visible');
    cy.contains(`Address: ${mockUser.address}`).should('be.visible');
    cy.contains(`Postal Code: ${mockUser.postalCode}`).should('be.visible');
    cy.contains(`Location: ${mockUser.city}`).should('be.visible');
  });

  it('displays profile buttons in read-only mode', () => {
    cy.get('.profile-buttons button.edit-info-button').should('be.visible');
    cy.get('.profile-buttons button.logout-button').should('be.visible');

    // Verify edit mode buttons are not visible
    cy.get('.profile-buttons button.save-info').should('not.exist');
    cy.get('.profile-buttons button.cancel-button').should('not.exist');
  });

  it('displays user listings', () => {
    cy.contains('Your listings').should('be.visible');
    cy.contains(mockItems[0].name).should('be.visible');
    cy.contains(mockItems[1].name).should('be.visible');
    cy.contains(`${mockItems[0].price} kr`).should('be.visible');
    cy.contains(`${mockItems[1].price} kr`).should('be.visible');
  });

  it('handles empty listings state', () => {
    // Intercept with empty items
    cy.intercept('GET', '**/api/user/item', {
      statusCode: 200,
      body: { items: [] }
    }).as('getEmptyItems');

    cy.visit('/profile');
    cy.wait('@getUserInfo');
    cy.wait('@getEmptyItems');

    cy.contains('You don\'t have any listings yet.').should('be.visible');
  });

  it('enters edit mode when edit button is clicked', () => {
    cy.get('.profile-buttons button.edit-info-button').click();

    // Verify form inputs are visible
    cy.get('input#firstName').should('be.visible').and('have.value', mockUser.name);
    cy.get('input#lastName').should('be.visible').and('have.value', mockUser.surname);
    cy.get('input#email').should('be.visible').and('have.value', mockUser.email);
    cy.get('input#countryCode').should('be.visible').and('have.value', `+${mockUser.countryCode}`);
    cy.get('input#phoneNumber').should('be.visible').and('have.value', `${mockUser.phoneNumber}`);
    cy.get('input#address').should('be.visible').and('have.value', mockUser.address);
    cy.get('input#postalCode').should('be.visible').and('have.value', `${mockUser.postalCode}`);
    cy.get('input#city').should('be.visible').and('have.value', mockUser.city);

    // Verify edit mode buttons
    cy.get('.profile-buttons button.save-info').should('be.visible');
    cy.get('.profile-buttons button.cancel-button').should('be.visible');
  });

  it('validates user input in edit mode', () => {
    cy.get('.profile-buttons button.edit-info-button').click();

    // Enter invalid data for first name
    cy.get('input#firstName').clear();
    cy.get('input#firstName').type('123');
    cy.contains('Only letters allowed.').should('be.visible');

    // Enter invalid email
    cy.get('input#email').clear();
    cy.get('input#email').type('invalid-email');
    cy.contains('Please enter a valid email.').should('be.visible');

    // Save button should be disabled
    cy.get('.profile-buttons button.save-info').should('be.disabled');

    // Fix the inputs
    cy.get('input#firstName').clear();
    cy.get('input#email').clear();

    cy.get('input#firstName').type('Jane');
    cy.get('input#email').type('jane.doe@example.com');

    // Save button should be enabled
    cy.get('.profile-buttons button.save-info').should('not.be.disabled');
  });

  it('can save updated user information', () => {
    // Mock the user update request
    cy.intercept('POST', '**/api/user/info', {
      statusCode: 200,
      body: {
        ...mockUser,
        name: 'Jane',
        surname: 'Smith'
      }
    }).as('updateUser');

    // Mock the subsequent user info fetch
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 200,
      body: {
        ...mockUser,
        name: 'Jane',
        surname: 'Smith'
      }
    }).as('getUpdatedUser');

    // Enter edit mode
    cy.get('.profile-buttons button.edit-info-button').click();

    // Change user data
    cy.get('input#firstName').clear();
    cy.get('input#lastName').clear();

    cy.get('input#firstName').type('Jane');
    cy.get('input#lastName').type('Smith');

    // Save changes
    cy.get('.profile-buttons button.save-info').click();

    // Verify the API calls
    cy.wait('@updateUser');
    cy.wait('@getUpdatedUser');

    // Verify the displayed data is updated
    cy.contains('Jane Smith').should('be.visible');
    cy.contains('First name: Jane').should('be.visible');
    cy.contains('Last name: Smith').should('be.visible');
  });

  it('can cancel edits without saving', () => {
    // Enter edit mode
    cy.get('.profile-buttons button.edit-info-button').click();

    // Change user data

    cy.get('input#firstName').clear();
    cy.get('input#lastName').clear();

    cy.get('input#firstName').type('Jane');
    cy.get('input#lastName').type('Smith');

    // Cancel changes
    cy.get('.profile-buttons button.cancel-button').click();

    // Verify original data is still displayed
    cy.contains(`${mockUser.name} ${mockUser.surname}`).should('be.visible');
    cy.contains(`First name: ${mockUser.name}`).should('be.visible');
    cy.contains(`Last name: ${mockUser.surname}`).should('be.visible');
  });

  it('shows logout confirmation when logout button is clicked', () => {
    cy.get('.profile-buttons button.logout-button').click();

    // Verify logout confirmation message
    cy.contains('Are you sure you want to sign out?').should('be.visible');

    // Verify confirmation buttons
    cy.get('.profile-buttons button.logout').should('be.visible');
    cy.get('.profile-buttons button.cancel-button').should('be.visible');
  });

  it('can cancel logout process', () => {
    cy.get('.profile-buttons button.logout-button').click();
    cy.get('.profile-buttons button.cancel-button').click();

    // Verify we're back to the normal profile view
    cy.get('.profile-buttons button.edit-info-button').should('be.visible');
    cy.get('.profile-buttons button.logout-button').should('be.visible');
  });

  it('can logout successfully', () => {
    // Mock logout API request
    cy.intercept('POST', '**/api/token/logout', {
      statusCode: 200
    }).as('logout');

    cy.get('.profile-buttons button.logout-button').click();
    cy.get('.profile-buttons button.logout').click();

    // Verify logout API call
    cy.wait('@logout');

    // Verify we're redirected to home
    cy.url().should('include', '/');
  });

  it('handles loading state correctly', () => {
    // Create intercepts with delay to test loading state
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 200,
      body: mockUser,
      delay: 1000
    }).as('delayedUserInfo');

    cy.intercept('GET', '**/api/user/item', {
      statusCode: 200,
      body: { items: mockItems },
      delay: 1500
    }).as('delayedUserItems');

    // Visit the page again to trigger the delayed responses
    cy.visit('/profile');

    // Check for loading states
    cy.contains('Loading your listings...').should('be.visible');

    // Wait for responses
    cy.wait('@delayedUserInfo');
    cy.wait('@delayedUserItems');

    // Check that content appears after loading
    cy.contains('Loading your listings...').should('not.exist');
    cy.contains(mockItems[0].name).should('be.visible');
  });

  it('redirects to login page when not authenticated', () => {
    // Mock failed auth check
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 401,
      body: { message: 'Not authenticated' }
    }).as('authFailed');

    // Visit profile page
    cy.visit('/profile');

    // Wait for the failed auth check
    cy.wait('@authFailed');

    // Verify redirect to login page
    cy.url().should('include', '/login');
  });

  it('adapts layout for mobile devices', () => {
    // Test with mobile viewport
    cy.viewport('iphone-6');
    cy.visit('/profile');
    cy.wait('@getUserInfo');
    cy.wait('@getUserItems');

    // Check if responsive classes are applied
    cy.get('.profile-image').should('be.visible');
    cy.get('.profile-details').should('be.visible');
  })

});
