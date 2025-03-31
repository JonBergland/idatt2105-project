
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

      expect(submitButton.disabled).toBe(true);
    });

    it("should have the submit button disabled when only one field is filled", async () => {
      const { getByPlaceholderText, getByRole } = render(LogInForm);

      const emailInput = getByPlaceholderText("Email");
      const passwordInput = getByPlaceholderText("Password");
      const submitButton = getByRole("button", { name: /log in/i });

      await fireEvent.update(emailInput, "test@example.com");
      expect(submitButton.disabled).toBe(true);

      await fireEvent.update(emailInput, "");
      await fireEvent.update(passwordInput, "password123");
      expect(submitButton.disabled).toBe(true);
    });

    it('calls handleLogin on form submission', async () => {
        const wrapper = mount(LogInForm)
        const handleLoginSpy = vi.spyOn(wrapper.vm, 'handleLogin')

        await wrapper.find('form').trigger('submit.prevent')
        expect(handleLoginSpy).toHaveBeenCalled()
    })


    it('displays error message on login failure', async () => {
        const wrapper = mount(LogInForm)
        const errorLabel = wrapper.find('label#login-status-label')

        await wrapper.find('form').trigger('submit.prevent')
        expect(errorLabel.text()).toContain('Log in failed:')
    })
})

