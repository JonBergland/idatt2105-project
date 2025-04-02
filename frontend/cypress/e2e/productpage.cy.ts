describe('Product Page View', () => {
  beforeEach(() => {
    cy.visit('/product/123') // Using arbitrary ID
  })

  it('displays the product name in the header', () => {
    cy.contains('Product name').should('be.visible')
  })

  it('displays the back button that navigates to home when clicked', () => {
    cy.get('.product-name-header img').first().click()

    cy.url().should('include', '/')
  })

  it('displays the product image', () => {
    cy.get('.product-image-container').should('be.visible')
    cy.get('.product-image').should('exist')
  })

  it('allows navigation between multiple product images', () => {
    cy.get('.nav-button.right').click()

    cy.get('.nav-button.left').click()
  })

  it('toggles favorite status when clicking the favorite button', () => {
  cy.get('.favorite-button img')
  .invoke('attr', 'src')
  .then(initialSrc => {
    cy.get('.favorite-button').click(); // Favorite product

    cy.get('.favorite-button img')
      .invoke('attr', 'src')
      .should('not.eq', initialSrc)
      .then(toggledSrc => {
        cy.get('.favorite-button').click(); // Unfavorite product

        cy.get('.favorite-button img')
          .invoke('attr', 'src')
          .should('not.eq', toggledSrc);
      });
  });
})

  it('has functioning bid and payment buttons', () => {
    cy.contains('button', 'Give a bid').should('be.visible')
    cy.contains('button', 'Pay with vipps').should('be.visible')

    cy.contains('button', 'Give a bid').click()
    cy.contains('button', 'Pay with vipps').click()
  })

  it('has the correct layout with image and info side by side', () => {
    cy.get('.product-info-wrapper').should('have.css', 'display', 'flex')

    cy.get('.product-image-wrapper').should('be.visible')
      .and('have.css', 'max-width', '50%')

    cy.get('.product-info-component-wrapper').should('be.visible')
      .and('have.css', 'max-width', '50%')
  })
})
