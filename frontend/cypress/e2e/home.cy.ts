describe('Home View', () => {
  // Mock data for testing
  const mockCategories = ['Electronics', 'Clothing', 'Books', 'Furniture'];
  const mockItems = Array(10).fill(null).map((_, i) => ({
    itemID: i + 1,
    name: `Test Item ${i + 1}`,
    price: 100 * (i + 1),
    category: mockCategories[i % mockCategories.length],
    seller: `Seller ${i + 1}`,
    description: `Description for item ${i + 1}`,
    published: new Date(2023, 3, 30 - i).toISOString().split('T')[0]
  }));

  const mockMoreItems = Array(5).fill(null).map((_, i) => ({
    itemID: i + 11,
    name: `More Item ${i + 1}`,
    price: 150 * (i + 1),
    category: mockCategories[i % mockCategories.length],
    seller: `More Seller ${i + 1}`,
    description: `More description ${i + 1}`,
    published: new Date(2023, 3, 20 - i).toISOString().split('T')[0]
  }));

  beforeEach(() => {
    // Set up API mocks
    cy.intercept('GET', '**/api/user/info', {
      statusCode: 200,
      body: { username: 'testuser', email: 'test@example.com' }
    }).as('getUserInfo');

    cy.intercept('GET', '**/api/store/category', {
      statusCode: 200,
      body: { categories: mockCategories }
    }).as('getCategories');

    // Stub initial items request
  cy.intercept('POST', '**/api/store/item/filter', {
    statusCode: 200,
    body: { items: mockItems }
  }).as('getItems');

  // Stub second page of items (for infinite scroll tests)
  cy.intercept('POST', '**/api/store/item/filter', (req) => {
    // Check if this is the second page request
    const isPageTwo = req.body && req.body.segmentOffset && req.body.segmentOffset[0] > 0;
    if (isPageTwo) {
      return {
        statusCode: 200,
        body: { items: mockMoreItems }
      };
    }
  }).as('getMoreItems');


    // Visit the home page
    cy.visit('/');

    // Wait for initial API requests to complete
    cy.wait('@getCategories');
    cy.wait('@getItems');
  });

  it('displays the welcome heading', () => {
    cy.contains('h1', 'Welcome to the Yard!').should('be.visible');
  });

  it('displays the search bar', () => {
    cy.get('.search-category-container').find('input').should('be.visible');
  });

  it('displays categories', () => {
    // At least one category should be displayed
    cy.get('.search-category-container').contains(mockCategories[0]).should('be.visible');
  });

  it('displays recommended items', () => {
    // Check heading
    cy.contains('h3', 'Recommendations').should('be.visible');

    // Check that items are displayed
    cy.get('div').contains(mockItems[0].name).should('be.visible');
    cy.get('div').contains(`${mockItems[0].price} kr`).should('be.visible');
  });

  it('navigates to result page on search', () => {
    const searchQuery = 'test search';

    // Type in search box and submit
    cy.get('.search-category-container input').type(searchQuery);
    cy.get('.input-container').find('button').click();

    // URL should change to result page with search query
    cy.url().should('include', 'http://localhost:4173/result?search=test+search&sort=published_DESC');
  });

  it('navigates to result page when category is clicked', () => {
    // Find and click a category
    cy.get('.search-category-container').contains(mockCategories[0]).click();

    // URL should change to result page with category
    cy.url().should('include', 'http://localhost:4173/result?category=Electronics&sort=published_DESC');
  });

  it('navigates to product page when item is clicked', () => {
    // Get the first item and click it
    cy.contains(mockItems[0].name).click();

    // URL should change to product page with item ID
    cy.url().should('include', `/product?id=${mockItems[0].itemID}`);
  });

  it('adapts layout for mobile devices', () => {
    // Test with mobile viewport
    cy.viewport('iphone-6');

    // Check if responsive classes are applied
    cy.get('.home-container').should('have.css', 'padding')
      .and('not.equal', '32px 128px');

    // Should still have critical elements visible
    cy.contains('h1', 'Welcome to the Yard!').should('be.visible');
    cy.get('.search-category-container input').should('be.visible');
    cy.contains(mockCategories[0]).should('be.visible');
  });

  it('maintains scroll position during navigation', () => {
    // Scroll halfway down the page
    cy.scrollTo(0, 300);

    // Click a category
    cy.get('.search-category-container').contains(mockCategories[0]).click();

    // Go back to home
    cy.go('back');

    // Check that components render again
    cy.contains('h1', 'Welcome to the Yard!').should('be.visible');

    // Wait for items to load
    cy.wait('@getItems');

    // Home page should be rendered with original content
    cy.contains(mockItems[0].name).should('be.visible');
  });

  it('handles search submission with Enter key', () => {
    const searchQuery = 'keyboard test';

    // Type in search box and press Enter
    cy.get('.search-category-container input').type(`${searchQuery}{enter}`);

    // URL should change to result page with search query
    cy.url().should('include', '/result?search=keyboard+test&sort=published_DESC');
  });

  it('gracefully handles loading states', () => {
    // Refresh page with delayed API responses
    cy.intercept('GET', '**/api/store/category', {
      statusCode: 200,
      body: { categories: mockCategories },
      delay: 1000 // 1 second delay
    }).as('slowCategories');

    cy.intercept('POST', '**/api/store/item/filter', {
      statusCode: 200,
      body: { items: mockItems },
      delay: 1500 // 1.5 second delay
    }).as('slowItems');

    cy.visit('/');

    // Page should still render core structure while data is loading
    cy.contains('h1', 'Welcome to the Yard!').should('be.visible');

    // Wait for slow responses
    cy.wait('@slowCategories');
    cy.wait('@slowItems');

    // Content should appear after loading
    cy.contains(mockCategories[0]).should('be.visible');
    cy.contains(mockItems[0].name).should('be.visible');
  });
});
