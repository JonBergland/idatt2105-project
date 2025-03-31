<script lang="ts">
import { ref, computed, defineComponent } from 'vue'
import "@/assets/color.css"
import "@/assets/base.css"

export default defineComponent({
  name: 'LogInForm',
  setup() {
    const email = ref("")
    const password = ref("")
    const errorLabelEl = ref<HTMLElement | null>(null)

    function verifyEmail() {
      const regex = /^[A-Za-z@.æøåÆØÅ]+$/;
      return regex.test(email.value.trim())
    }

    function verifyPassword() {
      return password.value.trim() !== ''
    }

    function setErrorLabel(errorMsg: string) {
      if (errorLabelEl.value) {
        errorLabelEl.value.textContent = "Log in failed: " + errorMsg
      }
      console.log("Log in failed: ", errorMsg);
    }

    const validEmail = computed(() => verifyEmail())
    const validPassword = computed(() => verifyPassword())
    const validForm = computed(() => validEmail.value && validPassword.value)

    async function handleLogin(event: Event) {
      if (!validForm.value) {
        event.preventDefault()
        setErrorLabel("Log in failed: Form invalid")
        return
      }

      const formData = {
        email: email.value,
        password: password.value
      }

      try {
        console.log(formData);
        // TODO: Add form submission logic
      } catch (error) {
        setErrorLabel(String(error))
      }
    }

    return {
      email,
      password,
      validForm,
      handleLogin,
      errorLabelEl,
    }
  },
})
</script>

<template>
  <div class="login-form" id="login-form">
    <form @submit.prevent="handleLogin">
      <label for="login">Please enter email and password:</label>
      <input class="input" type="text" id="email" name="email" placeholder="Email" v-model="email" required />
      <input class="input" type="text" id="password" name="password" placeholder="Password" v-model="password" required/>
      <input type="submit" :disabled="!validForm" value="Log in" id="login-button">
      <label for="error" id="login-status-label" ref="errorLabelEl"></label>
    </form>
  </div>
</template>

<style>

.login-form {
  display: grid;
  grid-template-columns: auto;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-soft);
}

.login-form form {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

#login-button {
  background-color: var(--color-primary);
  color: #FFFFFF;
  border-radius: var(--size-button-radius);
  padding: var(--size-button-padding);
  min-width: var(--size-button-min);
}

#login-button:hover {
  background-color: var(--color-primary-hover);
}

#login-button:disabled {
  background-color: var(--color-background-mute);
}

</style>
