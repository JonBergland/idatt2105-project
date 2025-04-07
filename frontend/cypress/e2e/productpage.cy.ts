describe('Product Page View', () => {
  const mockProduct = {
    itemID: 123,
    name: 'Test Product',
    price: 1000,
    category: 'Electronics',
    seller: 'John Doe',
    description: 'This is a test product description.',
    published: '2023-04-01',
    state: 'available',
    images: []
  };

  beforeEach(() => {
    // Stub the API request for fetching item details
    cy.intercept('POST', '**/api/store/item/get', {
      statusCode: 200,
      body: mockProduct
    }).as('getItemDetails');

    // Visit the product page with a specific item ID
    cy.visit('/product?id=123');

    // Wait for the API call to complete
    cy.wait('@getItemDetails');
  });

  it('displays the product name correctly', () => {
    cy.contains(mockProduct.name).should('be.visible');
  });

  it('displays product information correctly', () => {
    // Price
    cy.contains(`${mockProduct.price} kr`).should('be.visible');

    // Category
    cy.contains(`Category: ${mockProduct.category}`).should('be.visible');

    // Seller
    cy.contains(`Name of seller: ${mockProduct.seller}`).should('be.visible');

    // Description
    cy.contains(`Description: ${mockProduct.description}`).should('be.visible');

    // Published date
    cy.contains(`Published: ${mockProduct.published}`).should('be.visible');

    // Availability state
    cy.contains('Available').should('be.visible');
  });

  it('displays product image', () => {
    cy.get('#product-image-component').should('be.visible');
    cy.get('#product-image-component img').should('be.visible');
  });

  it('has the correct layout structure', () => {
    // Verify overall wrapper
    cy.get('.product-page-wrapper').should('be.visible');

    // Verify info wrapper with flex display
    cy.get('.product-info-wrapper')
      .should('be.visible')
      .should('have.css', 'display', 'flex');

    // Verify image and info areas with correct max-width
    cy.get('.product-image-wrapper')
      .should('be.visible')
      .should('have.css', 'max-width', '50%');

    cy.get('.product-info-component-wrapper')
      .should('be.visible')
      .should('have.css', 'max-width', '50%');
  });

  it('handles responsive layout on mobile devices', () => {
    // Set viewport to mobile size
    cy.viewport('iphone-6');

    // Verify the component is still visible
    cy.get('.product-page-wrapper').should('be.visible');

    // You may need to add additional checks for your specific mobile layout
  });

  it('handles loading state correctly', () => {
    // Create a new intercept with delay to test loading state
    cy.intercept('POST', '**/api/store/item/get', {
      statusCode: 200,
      body: mockProduct,
      delay: 1000 // 1 second delay
    }).as('delayedItemDetails');

    // Visit page again
    cy.visit('/product?id=123');

    // Component should not be visible during loading
    cy.get('.product-page-wrapper').should('not.exist');

    // Wait for response and check if component appears
    cy.wait('@delayedItemDetails');
    cy.get('.product-page-wrapper').should('be.visible');
  });

  describe('Product States', () => {
    it('displays "Reserved" when product is reserved', () => {
      const reservedProduct = { ...mockProduct, state: 'reserved' };

      cy.intercept('POST', '**/api/store/item/get', {
        statusCode: 200,
        body: reservedProduct
      }).as('getReservedItem');

      cy.visit('/product?id=123');
      cy.wait('@getReservedItem');

      cy.contains('Reserved').should('be.visible');
    });

    it('displays "Not Available: Sold" when product is sold', () => {
      const soldProduct = { ...mockProduct, state: 'sold' };

      cy.intercept('POST', '**/api/store/item/get', {
        statusCode: 200,
        body: soldProduct
      }).as('getSoldItem');

      cy.visit('/product?id=123');
      cy.wait('@getSoldItem');

      cy.contains('Not Available: Sold').should('be.visible');
    });
  });
});
