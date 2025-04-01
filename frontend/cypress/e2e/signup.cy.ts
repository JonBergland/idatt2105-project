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
    cy.get('input#signup-button').should('exist')
      .and('be.disabled')
  })

  it('shows validation errors when submitting with empty fields', () => {
    cy.get('input#signup-button').should('be.disabled')

    cy.get('input#email').click()
    cy.get('input#email').blur()

    cy.get('input#firstName').click()
    cy.get('input#firstName').blur()

    cy.get('input#lastName').click()
    cy.get('input#lastName').blur()

    cy.get('input#landCode').click()
    cy.get('input#landCode').blur()

    cy.get('input#phoneNr').click()
    cy.get('input#phoneNr').blur()

    cy.get('input#password').click()
    cy.get('input#password').blur()

    cy.get('input#repeatPassword').click()
    cy.get('input#repeatPassword').blur()

    cy.get('.error-msg').should('have.length.at.least', 1)
  })

  it('validates email format', () => {
    cy.get('input#email').clear()
    cy.get('input#email').type('invalid-email')
    cy.get('input#email').blur()
    cy.contains('Please enter a valid email.').should('exist')

    cy.get('input#email').clear()
    cy.get('input#email').type('valid@example.com')
    cy.get('input#email').blur()
    cy.contains('Please enter a valid email.').should('not.exist')
  })

  it('validates name fields only accept letters', () => {
    cy.get('input#firstName').type('John123')
    cy.get('input#firstName').blur()
    cy.contains('Only letters allowed.').should('exist')

    cy.get('input#firstName').clear()
    cy.get('input#firstName').type('John')
    cy.get('input#firstName').blur()
    cy.contains('Only letters allowed.').should('not.exist')

    cy.get('input#lastName').type('Doe456')
    cy.get('input#lastName').blur()
    cy.contains('Only letters allowed.').should('exist')

    cy.get('input#lastName').clear()
    cy.get('input#lastName').type('Doe')
    cy.get('input#lastName').blur()
    cy.contains('Only letters allowed.').should('not.exist')
  })

  it('validates phone fields', () => {
    cy.get('input#landCode').clear()
    cy.get('input#landCode').type('abc')
    cy.get('input#landCode').blur()
    cy.get('input#phoneNr').type('123456')
    cy.get('input#phoneNr').blur()
    cy.contains('Please enter a valid phone number.').should('exist')

    cy.get('input#landCode').clear()
    cy.get('input#landCode').type('+1')
    cy.get('input#landCode').blur()

    cy.get('input#phoneNr').clear()
    cy.get('input#phoneNr').type('1234567890')
    cy.get('input#phoneNr').blur()
    cy.contains('Please enter a valid phone number.').should('not.exist')

    cy.get('input#landCode').clear()
    cy.get('input#landCode').type('47')
    cy.get('input#landCode').blur()
    cy.get('input#landCode').should('have.value', '+47')
  })

  it('validates passwords match', () => {
    cy.get('input#password').type('password123')
    cy.get('input#password').blur()
    cy.get('input#repeatPassword').type('password456')
    cy.get('input#repeatPassword').blur()
    cy.contains('Passwords do not match').should('exist')

    cy.get('input#repeatPassword').clear()
    cy.get('input#repeatPassword').type('password123')
    cy.get('input#repeatPassword').blur()
    cy.contains('Passwords do not match').should('not.exist')
  })

  it('enables submit button when all fields are valid', () => {
    cy.get('input#email').type('test@example.com')
    cy.get('input#email').blur()
    cy.get('input#firstName').type('John')
    cy.get('input#firstName').blur()
    cy.get('input#lastName').type('Doe')
    cy.get('input#lastName').blur()
    cy.get('input#landCode').clear()
    cy.get('input#landCode').type('+1')
    cy.get('input#landCode').blur()
    cy.get('input#phoneNr').type('1234567890')
    cy.get('input#phoneNr').blur()
    cy.get('input#password').type('password123')
    cy.get('input#password').blur()
    cy.get('input#repeatPassword').type('password123')
    cy.get('input#repeatPassword').blur()

    cy.get('input#signup-button').should('not.be.disabled')
  })

  /* TODO: Add submission test when API is implemented  */
})
