<script setup lang="ts">
import { ref, computed } from 'vue'
import * as stringVerificationUtils from '@/utils/stringVerificationUtils'
import "@/assets/color.css"
import "@/assets/base.css"


const email = ref("")
const firstName = ref("")
const lastName = ref("")
const landCode = ref("")
const phoneNr = ref("")
const password = ref("")
const repeatPassword = ref("")
const errorLabelEl = ref<HTMLElement | null>(null)

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
const validFirstName = computed(() => stringVerificationUtils.verifyStringForLetters(firstName.value))
const validLastName = computed(() => stringVerificationUtils.verifyStringForLetters(lastName.value))
const validLandCode = computed(() => stringVerificationUtils.verifyStringForNumbers(landCode.value))
const validPhoneNumber = computed(() => stringVerificationUtils.verifyStringForNumbers(phoneNr.value))
const validPassword = computed(() => stringVerificationUtils.verifyStringNotEmpty(password.value))
const validRepeatPassword = computed(() => stringVerificationUtils.verifyStringNotEmpty(repeatPassword.value))

const validForm = computed(() => {
  validEmail.value && validFirstName.value && validLastName.value && validLandCode.value
  && validPhoneNumber.value && validPassword.value && validRepeatPassword.value
})

/**
 * Handles the registration process when the registration form is submitted.
 *
 * @param {Event} event - The event object triggered by the form submission.
 * @returns {Promise<void>} A promise that resolves when the registration process is complete.
 */
async function handleRegistration(event: Event) {
  if (!validForm.value) {
    event.preventDefault()
    setErrorLabel("Log in failed: Form invalid")
    return
  }

  const formData = {
    email: email.value,
    firstName: firstName.value,
    lastName: lastName.value,
    landCode: landCode.value,
    phoneNr: phoneNr.value,
    repeatPassword: repeatPassword.value,
    password: password.value
  }

  try {
    console.log(formData);
    // TODO: Add form submission logic
  } catch (error) {
    setErrorLabel(String(error))
  }
}

// Expose properties for testing
defineExpose({
  email,
  password,
  validForm,
  handleRegistration,
  errorLabelEl
})
</script>

<template>
  <div class="registration-form" id="registration-form">
    <form @submit.prevent="handleRegistration">
      <label for="registration">Please fill out the fields below:</label>
      <input class="input" type="text" id="email" name="email" placeholder="Email" v-model="email" required />
      <input class="input" type="text" id="firstName" name="firstName" placeholder="First Name" v-model="firstName" required />
      <input class="input" type="text" id="lastName" name="lastName" placeholder="Last Name" v-model="lastName" required />
      <input class="input" type="text" id="landCode" name="landCode" placeholder="Land Code" v-model="landCode" required />
      <input class="input" type="text" id="phoneNr" name="phoneNr" placeholder="Phone Number" v-model="phoneNr" required />
      <input class="input" type="password" id="password" name="password" placeholder="Password" v-model="password" required />
      <input class="input" type="password" id="repeatPassword" name="repeatPassword" placeholder="Repeat Password" v-model="repeatPassword" required />
      <input type="submit" :disabled="!validForm" value="Sign up" id="singup-button">
      <label for="error" id="registration-status-label" ref="errorLabelEl"></label>
    </form>
  </div>
</template>

<style>

.registration-form {
  display: grid;
  grid-template-columns: auto;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-soft);
}

.registration-form form {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

#signup-button {
  background-color: var(--color-primary);
  color: #FFFFFF;
  border-radius: var(--size-button-radius);
  padding: var(--size-button-padding);
  min-width: var(--size-button-min);
}

#signup-button:hover {
  background-color: var(--color-primary-hover);
}

#signup-button:disabled {
  background-color: var(--color-background-mute);
}

</style>
