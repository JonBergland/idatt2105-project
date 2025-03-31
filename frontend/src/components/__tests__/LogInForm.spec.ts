import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, fireEvent, screen, cleanup } from "@testing-library/vue";
import { mount } from '@vue/test-utils'
import LogInForm from '@/components/LogIn/LogInForm.vue'

afterEach(cleanup);

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

        await emailInput.setValue('test@example.com')
        await passwordInput.setValue('password123')

        expect(wrapper.vm.email).toBe('test@example.com')
        expect(wrapper.vm.password).toBe('password123')
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

      await fireEvent.update(emailInput, "test@example.com");
      expect((submitButton as HTMLButtonElement).disabled).toBe(true);

      await fireEvent.update(emailInput, "");
      await fireEvent.update(passwordInput, "password123");
      expect((submitButton as HTMLButtonElement).disabled).toBe(true);
    });

    // TODO: Fix tests to better fit backend functionality
    it('processes form data on submission', async () => {
      const wrapper = mount(LogInForm)

      const emailInput = wrapper.find('input#email')
      const passwordInput = wrapper.find('input#password')

      await emailInput.setValue('test@example.com')
      await passwordInput.setValue('password123')

      const consoleSpy = vi.spyOn(console, 'log')

      await wrapper.find('form').trigger('submit')

      expect(consoleSpy).toHaveBeenCalledWith(expect.objectContaining({
          email: 'test@example.com',
          password: 'password123'
      }))

      consoleSpy.mockRestore()
    })

    it('displays error message on login failure', async () => {
        const wrapper = mount(LogInForm)

        const errorLabel = wrapper.find('#login-status-label')

        const mockErrorEl = document.createElement('label')
        wrapper.vm.errorLabelEl = mockErrorEl

        await wrapper.vm.handleLogin(new Event('submit'))

        // Need to wait for DOM update
        await wrapper.vm.$nextTick()

        expect(mockErrorEl.textContent).toContain('Log in failed:')
    })
})

