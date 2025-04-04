<script setup lang="ts">
import { ref, computed } from 'vue'
import * as stringVerificationUtils from '@/utils/stringVerificationUtils'
import "@/assets/color.css"
import "@/assets/base.css"
import type { UserLoginDTO } from '@/models/user'

const email = ref("")
const password = ref("")
const errorLabelEl = ref<HTMLElement | null>(null)

const emit = defineEmits(["login"])

/**
 * Sets the error label with the parameter errormessage
 *
 * @param errorMsg
 * @returns {void}
 */
function setErrorLabel(errorMsg: string) {
  if (errorLabelEl.value) {
    errorLabelEl.value.textContent = "Log in failed: " + errorMsg
  }
  console.log("Log in failed: ", errorMsg);
}

const validEmail = computed(() => stringVerificationUtils.verifyStringForEmail(email.value))
const validPassword = computed(() => stringVerificationUtils.verifyStringNotEmpty(password.value))
const validForm = computed(() => validEmail.value && validPassword.value)

/**
 * Handles the login process when the login form is submitted.
 *
 * @param {Event} event - The event object triggered by the form submission.
 * @returns {Promise<void>} A promise that resolves when the login process is complete.
 */
async function handleLogin(event: Event) {
  if (!validForm.value) {
    event.preventDefault()
    setErrorLabel("Log in failed: Form invalid")
    return
  }

  try {
    const formData: UserLoginDTO = {
    email: email.value,
    password: password.value
    }

    emit("login", formData)
  } catch (error) {
    setErrorLabel(String(error))
  }
}

// Expose properties for testing
defineExpose({
  email,
  password,
  validForm,
  handleLogin,
  errorLabelEl
})
</script>

<template>
  <div class="login-form" id="login-form">
    <form @submit.prevent="handleLogin">
      <label for="login">Please enter email and password:</label>
      <input class="input" type="text" id="email" name="email" placeholder="Email" v-model="email" required />
      <input class="input" type="password" id="password" name="password" placeholder="Password" v-model="password" required/>
      <input type="submit" :disabled="!validForm" value="Log in" id="login-button">
      <label for="error" id="login-status-label" ref="errorLabelEl"></label>
    </form>
  </div>
</template>

<style>

.login-form {
  display: grid;
  grid-template-rows: 1fr;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-soft);
}

.login-form form {
  display: grid;
  grid-template-rows: auto;
  place-items: center;
  gap: 15px;
}

.input {
  width: 80%;
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
  color: var(--color-button-disabled);
}

</style>
