import { describe, it, expect, afterEach } from 'vitest'
import { render, fireEvent, screen, cleanup } from "@testing-library/vue";
import { mount } from '@vue/test-utils'
import SignUpForm from '@/components/SignUp/SignUpForm.vue'
import { UserRegistrationDTO } from '@/models/user'

afterEach(cleanup);

const testEmail = 'test@example.com';
const testFirstName = 'John';
const testLastName = 'Doe';
const testLandCode = '+1';
const testPhoneNr = '1234567890';
const testPassword = 'password123';
const testRepeatPassword = 'password123';

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

    await emailInput.setValue(testEmail);
    await firstNameInput.setValue(testFirstName);
    await lastNameInput.setValue(testLastName);
    await landCodeInput.setValue(testLandCode);
    await phoneNrInput.setValue(testPhoneNr);
    await passwordInput.setValue(testPassword);
    await repeatPasswordInput.setValue(testRepeatPassword);

    expect(wrapper.vm.email).toBe(testEmail)
    expect(wrapper.vm.firstName).toBe(testFirstName)
    expect(wrapper.vm.lastName).toBe(testLastName)
    expect(wrapper.vm.landCode).toBe(testLandCode)
    expect(wrapper.vm.phoneNr).toBe(testPhoneNr)
    expect(wrapper.vm.password).toBe(testPassword)
    expect(wrapper.vm.repeatPassword).toBe(testRepeatPassword)

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

    await fireEvent.update(emailInput, testEmail);
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);

    await fireEvent.update(firstNameInput, testFirstName);
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);
  })

  it('emits user data on form submission', async () => {
    const wrapper = mount(SignUpForm)

    const emailInput = wrapper.find('input#email')
    const firstNameInput = wrapper.find('input#firstName')
    const lastNameInput = wrapper.find('input#lastName')
    const landCodeInput = wrapper.find('input#landCode')
    const phoneNrInput = wrapper.find('input#phoneNr')
    const passwordInput = wrapper.find('input#password')
    const repeatPasswordInput = wrapper.find('input#repeatPassword')

    await emailInput.setValue(testEmail)
    await firstNameInput.setValue(testFirstName)
    await lastNameInput.setValue(testLastName)
    await landCodeInput.setValue(testLandCode)
    await phoneNrInput.setValue(testPhoneNr)
    await passwordInput.setValue(testPassword)
    await repeatPasswordInput.setValue(testRepeatPassword)

    await wrapper.find('form').trigger('submit')

    expect(wrapper.emitted().signup).toBeTruthy()
    expect((wrapper.emitted().signup![0][0] as UserRegistrationDTO)).toEqual({
      email: testEmail,
      name: testFirstName,
      surname: testLastName,
      countryCode: parseInt(testLandCode.replace('+', '')),
      phoneNumber: parseInt(testPhoneNr),
      password: testPassword,
    })
  })

  it('displays error message on registration failure', async () => {
    const wrapper = mount(SignUpForm)

    const mockErrorEl = document.createElement('label')
    wrapper.vm.errorLabelEl = mockErrorEl

    await wrapper.vm.handleRegistration(new Event('submit'))

    await wrapper.vm.$nextTick()

    expect(mockErrorEl.textContent).toContain('Registration failed:')
  })

  it('validates password match', async () => {
    const wrapper = mount(SignUpForm)

    const emailInput = wrapper.find('input#email')
    const firstNameInput = wrapper.find('input#firstName')
    const lastNameInput = wrapper.find('input#lastName')
    const landCodeInput = wrapper.find('input#landCode')
    const phoneNrInput = wrapper.find('input#phoneNr')
    const passwordInput = wrapper.find('input#password')
    const repeatPasswordInput = wrapper.find('input#repeatPassword')

    await emailInput.setValue(testEmail)
    await firstNameInput.setValue(testFirstName)
    await lastNameInput.setValue(testLastName)
    await landCodeInput.setValue(testLandCode)
    await phoneNrInput.setValue(testPhoneNr)
    await passwordInput.setValue(testPassword)
    await repeatPasswordInput.setValue('different-password')

    expect(wrapper.vm.validForm).toBe(false)
  })

  describe('SignUpForm verifyLandcode', () => {
    it('returns true when landcode is correct', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue(testLandCode)

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
      await landCodeInput.setValue(testLandCode.replace('+', ''))

      expect(wrapper.vm.verifyLandCode()).toBe(true)
      expect(wrapper.vm.landCode).toContain('+')
    })

    it('removes one + when multiple are in input', async () => {
      const wrapper = mount(SignUpForm)

      const landCodeInput = wrapper.find('input#landCode')
      await landCodeInput.setValue('++' + testLandCode.replace('+', ''))

      expect(wrapper.vm.verifyLandCode()).toBe(true)
      expect(wrapper.vm.landCode).toEqual(testLandCode)
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
