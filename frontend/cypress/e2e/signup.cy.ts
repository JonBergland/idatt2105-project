describe('Signup Form', () => {
  beforeEach(() => {
    cy.visit('/signup')
  })

  it('displays the signup form', () => {
    cy.get('#registration-form').should('exist')
    cy.get('input#email').should('exist')
    cy.get('input#firstName').should('exist')
    cy.get('input#lastName').should('exist')
    cy.get('input#landCode').should('exist')
    cy.get('input#phoneNr').should('exist')
    cy.get('input#password').should('exist')
    cy.get('input#repeatPassword').should('exist')
    cy.get('input#signup-button').should('exist').and('be.disabled')
  })

  it('shows validation errors when submitting with empty fields', () => {
    cy.get('input#signup-button').should('be.disabled')

    cy.get('input#email').click().blur()
    cy.get('input#firstName').click().blur()
    cy.get('input#lastName').click().blur()
    cy.get('input#landCode').click().blur()
    cy.get('input#phoneNr').click().blur()
    cy.get('input#password').click().blur()
    cy.get('input#repeatPassword').click().blur()

    cy.get('.error-msg').should('have.length.at.least', 1)
  })

  it('validates email format', () => {
    cy.get('input#email').type('invalid-email').blur()
    cy.contains('Please enter a valid email.').should('exist')

    cy.get('input#email').clear().type('valid@example.com').blur()
    cy.contains('Please enter a valid email.').should('not.exist')
  })

  it('validates name fields only accept letters', () => {
    cy.get('input#firstName').type('John123').blur()
    cy.contains('Only letters allowed.').should('exist')

    cy.get('input#firstName').clear().type('John').blur()
    cy.contains('Only letters allowed.').should('not.exist')

    cy.get('input#lastName').type('Doe456').blur()
    cy.contains('Only letters allowed.').should('exist')

    cy.get('input#lastName').clear().type('Doe').blur()
    cy.contains('Only letters allowed.').should('not.exist')
  })

  it('validates phone fields', () => {
    cy.get('input#landCode').clear().type('abc').blur()
    cy.get('input#phoneNr').type('123456').blur()
    cy.contains('Please enter a valid phone number.').should('exist')

    cy.get('input#landCode').clear().type('+1').blur()
    cy.get('input#phoneNr').clear().type('1234567890').blur()
    cy.contains('Please enter a valid phone number.').should('not.exist')

    cy.get('input#landCode').clear().type('47').blur()
    cy.get('input#landCode').should('have.value', '+47')
  })

  it('validates passwords match', () => {
    cy.get('input#password').type('password123').blur()
    cy.get('input#repeatPassword').type('password456').blur()
    cy.contains('Passwords do not match').should('exist')

    cy.get('input#repeatPassword').clear().type('password123').blur()
    cy.contains('Passwords do not match').should('not.exist')
  })

  it('enables submit button when all fields are valid', () => {
    cy.get('input#email').type('test@example.com')
    cy.get('input#firstName').type('John')
    cy.get('input#lastName').type('Doe')
    cy.get('input#landCode').clear().type('+1')
    cy.get('input#phoneNr').type('1234567890')
    cy.get('input#password').type('password123')
    cy.get('input#repeatPassword').type('password123')

    cy.get('input#signup-button').should('not.be.disabled')
  })

  /* TODO: Add submission test when API is implemented  */
})
