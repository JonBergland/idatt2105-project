import { describe, it, expect, afterEach } from 'vitest'
import { render, fireEvent, screen, cleanup } from "@testing-library/vue";
import { mount } from '@vue/test-utils'
import LogInForm from '@/components/LogIn/LogInForm.vue'
import { UserLoginDTO } from '@/models/user'

afterEach(cleanup);

const email = "test@example.com"
const password = "password123"

describe('LogInForm', () => {
  it('renders the login form', () => {
    const wrapper = mount(LogInForm)
    expect(wrapper.find('form').exists()).toBe(true)
    expect(wrapper.find('input#email').exists()).toBe(true)
    expect(wrapper.find('input#password').exists()).toBe(true)
    expect(wrapper.find('input#login-button').exists()).toBe(true)
    expect(wrapper.find('input#login-button').attributes('disabled')).not.toBeUndefined();
  })

  it('binds email and password inputs correctly', async () => {
    const wrapper = mount(LogInForm)
    const emailInput = wrapper.find('input#email')
    const passwordInput = wrapper.find('input#password')

    await emailInput.setValue(email)
    await passwordInput.setValue(password)

    expect(wrapper.vm.email).toBe(email)
    expect(wrapper.vm.password).toBe(password)
  })

  it("should have the submit button disabled when fields are empty", async () => {
    render(LogInForm);
    const submitButton = screen.getByRole("button", { name: /log in/i });
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);
  });

  it("should have the submit button disabled when only one field is filled", async () => {
    const { getByPlaceholderText, getByRole } = render(LogInForm);
    const emailInput = getByPlaceholderText("Email");
    const passwordInput = getByPlaceholderText("Password");
    const submitButton = getByRole("button", { name: /log in/i });

    await fireEvent.update(emailInput, email);
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);

    await fireEvent.update(emailInput, "");
    await fireEvent.update(passwordInput, password);
    expect((submitButton as HTMLButtonElement).disabled).toBe(true);
  });

  // TODO: Fix tests to better fit backend functionality
  it('processes form data on submission', async () => {
    const wrapper = mount(LogInForm)

    const emailInput = wrapper.find('input#email')
    const passwordInput = wrapper.find('input#password')

    await emailInput.setValue(email)
    await passwordInput.setValue(password)

    await wrapper.find('form').trigger('submit')

    expect(wrapper.emitted().login).toBeTruthy()
    expect((wrapper.emitted().login![0][0] as UserLoginDTO)).toEqual({
          email: email,
          password: password,
        })
  })

  it('displays error message on login failure', async () => {
    const wrapper = mount(LogInForm)

    const mockErrorEl = document.createElement('label')
    wrapper.vm.errorLabelEl = mockErrorEl

    await wrapper.vm.handleLogin(new Event('submit'))

    // Need to wait for DOM update
    await wrapper.vm.$nextTick()

    expect(mockErrorEl.textContent).toContain('Log in failed:')
  })
})
