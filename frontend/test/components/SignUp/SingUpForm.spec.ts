import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, fireEvent, screen, cleanup } from "@testing-library/vue";
import { mount } from '@vue/test-utils'
import SignUpForm from '@/components/SignUp/SignUpForm.vue'

afterEach(cleanup);

describe('SignUpForm', () => {
  it('renders the registration form', () => {
    const wrapper = mount(SignUpForm)
    expect(wrapper.find('form').exists()).toBe(true)
    expect(wrapper.find('input#email').exists()).toBe(true)
    expect(wrapper.find('input#firstName').exists()).toBe(true)
    expect(wrapper.find('input#lastName').exists()).toBe(true)
    expect(wrapper.find('input#landCode').exists()).toBe(true)
    expect(wrapper.find('input#phoneNr').exists()).toBe(true)
    expect(wrapper.find('input#password').exists()).toBe(true)
    expect(wrapper.find('input#repeatPassword').exists()).toBe(true)
    expect(wrapper.find('input#signup-button').exists()).toBe(true)
    expect(wrapper.find('input#signup-button').attributes('disabled')).not.toBeUndefined();
  })

  it('binds form inputs correctly', async () => {
    const wrapper = mount(SignUpForm)

    const emailInput = wrapper.find('input#email')
    const firstNameInput = wrapper.find('input#firstName')
    const lastNameInput = wrapper.find('input#lastName')
    const landCodeInput = wrapper.find('input#landCode')
    const phoneNrInput = wrapper.find('input#phoneNr')
    const passwordInput = wrapper.find('input#password')
    const repeatPasswordInput = wrapper.find('input#repeatPassword')

    await emailInput.setValue('test@example.com')
    await firstNameInput.setValue('John')
    await lastNameInput.setValue('Doe')
    await landCodeInput.setValue('+1')
    await phoneNrInput.setValue('1234567890')
    await passwordInput.setValue('password123')
    await repeatPasswordInput.setValue('password123')

    expect(wrapper.vm.email).toBe('test@example.com')
    expect(wrapper.vm.firstName).toBe('John')
    expect(wrapper.vm.lastName).toBe('Doe')
    expect(wrapper.vm.landCode).toBe('+1')
    expect(wrapper.vm.phoneNr).toBe('1234567890')
    expect(wrapper.vm.password).toBe('password123')
    expect(wrapper.vm.repeatPassword).toBe('password123')

    // Check if the submit button is enabled when all fields are set
    const submitButton = wrapper.find('input#signup-button');
    expect((submitButton.element as HTMLButtonElement).disabled).toBe(false);
  })

  it('should have the submit button disabled when fields are empty', async () => {
    render(SignUpForm);
    const submitButton = screen.getByRole("button", { name: /sign up/i });
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);
  })

  it('should have the submit button disabled when only some fields are filled', async () => {
    const { getByPlaceholderText, getByRole } = render(SignUpForm);

    const emailInput = getByPlaceholderText("Email");
    const firstNameInput = getByPlaceholderText("First Name");
    const submitButton = getByRole("button", { name: /sign up/i });

    await fireEvent.update(emailInput, "test@example.com");
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);

    await fireEvent.update(firstNameInput, "John");
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);
  })

  it('processes form data on submission', async () => {
    const wrapper = mount(SignUpForm)

    // Fill all required fields
    const emailInput = wrapper.find('input#email')
    const firstNameInput = wrapper.find('input#firstName')
    const lastNameInput = wrapper.find('input#lastName')
    const landCodeInput = wrapper.find('input#landCode')
    const phoneNrInput = wrapper.find('input#phoneNr')
    const passwordInput = wrapper.find('input#password')
    const repeatPasswordInput = wrapper.find('input#repeatPassword')

    await emailInput.setValue('test@example.com')
    await firstNameInput.setValue('John')
    await lastNameInput.setValue('Doe')
    await landCodeInput.setValue('+1')
    await phoneNrInput.setValue('1234567890')
    await passwordInput.setValue('password123')
    await repeatPasswordInput.setValue('password123')

    const consoleSpy = vi.spyOn(console, 'log')

    await wrapper.find('form').trigger('submit')

    expect(consoleSpy).toHaveBeenCalledWith(expect.objectContaining({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      landCode: '+1',
      phoneNr: '1234567890',
      password: 'password123',
      repeatPassword: 'password123'
    }))

    consoleSpy.mockRestore()
  })

  it('displays error message on registration failure', async () => {
    const wrapper = mount(SignUpForm)

    const mockErrorEl = document.createElement('label')
    wrapper.vm.errorLabelEl = mockErrorEl

    await wrapper.vm.handleRegistration(new Event('submit'))

    // Wait for DOM update
    await wrapper.vm.$nextTick()

    expect(mockErrorEl.textContent).toContain('Registration failed:')
  })

  it('validates password match', async () => {
    const wrapper = mount(SignUpForm)

    // Fill form but have mismatching passwords
    const emailInput = wrapper.find('input#email')
    const firstNameInput = wrapper.find('input#firstName')
    const lastNameInput = wrapper.find('input#lastName')
    const landCodeInput = wrapper.find('input#landCode')
    const phoneNrInput = wrapper.find('input#phoneNr')
    const passwordInput = wrapper.find('input#password')
    const repeatPasswordInput = wrapper.find('input#repeatPassword')

    await emailInput.setValue('test@example.com')
    await firstNameInput.setValue('John')
    await lastNameInput.setValue('Doe')
    await landCodeInput.setValue('+1')
    await phoneNrInput.setValue('1234567890')
    await passwordInput.setValue('password123')
    await repeatPasswordInput.setValue('different-password')

    expect(wrapper.vm.validForm).toBe(false)
  })

  // TODO: Add tests for verifying sending to backend

  describe('SignUpForm verifyLandcode', () => {
    it('returns true when landcode is correct', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue('+1')

      expect(wrapper.vm.verifyLandCode()).toBe(true)
    })

    it('returns false when landcode is wrong', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue('+Non')

      expect(wrapper.vm.verifyLandCode()).toBe(false)
    })

    it('adds + when missing from input', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue('32')

      expect(wrapper.vm.verifyLandCode()).toBe(true)
      expect(wrapper.vm.landCode).toContain('+')
    })

    it('removes one + when multiple are in input', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue('++32')

      expect(wrapper.vm.verifyLandCode()).toBe(true)
      expect(wrapper.vm.landCode).toEqual('+32')
    })
  })

  describe('SignUpForm setErrorlable', () => {
    it('assigns the error message to the label', async () => {
      const wrapper = mount(SignUpForm)

      const mockErrorEl = document.createElement('label')
      wrapper.vm.errorLabelEl = mockErrorEl

      const errorMsg = "An error happend"
      wrapper.vm.setErrorLabel(errorMsg)

      expect(mockErrorEl.textContent).toContain(errorMsg)
    })
  })
})
