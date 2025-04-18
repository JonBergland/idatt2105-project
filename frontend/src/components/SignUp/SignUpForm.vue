<script setup lang="ts">
import { ref, computed } from 'vue'
import * as stringVerificationUtils from '@/utils/stringVerificationUtils'
import "@/assets/color.css"
import "@/assets/base.css"
import type { UserRegistrationDTO } from '@/models/user';

const emit = defineEmits(["signup"])

const email = ref("")
const firstName = ref("")
const lastName = ref("")
const landCode = ref("+47")
const phoneNr = ref("")
const password = ref("")
const repeatPassword = ref("")

const emailTouched = ref(false)
const firstNameTouched = ref(false)
const lastNameTouched = ref(false)
const landCodeTouched = ref(false)
const phoneNrTouched = ref(false)
const passwordTouched = ref(false)
const repeatPasswordTouched = ref(false)

const errorLabelEl = ref<HTMLElement | null>(null)

const validEmail = computed(() => stringVerificationUtils.verifyStringForEmail(email.value))
const validFirstName = computed(() => stringVerificationUtils.verifyStringForLetters(firstName.value))
const validLastName = computed(() => stringVerificationUtils.verifyStringForLetters(lastName.value))
const validLandCode = computed(() => verifyLandCode())
const validPhoneNumber = computed(() => stringVerificationUtils.verifyStringForNumbers(phoneNr.value))
const validPassword = computed(() => stringVerificationUtils.verifyStringNotEmpty(password.value))

/**
 * A computed property that determines if the sign-up form is valid.
 * This property evaluates the current state of the form fields and
 * returns a boolean indicating whether all validation criteria are met.
 */
const validForm = computed(() => {
  return validEmail.value &&
    validFirstName.value &&
    validLastName.value &&
    validLandCode.value &&
    validPhoneNumber.value &&
    validPassword.value &&
    (password.value === repeatPassword.value)
})

/**
 * Verifies the validity of a land code.
 * This function checks if the provided land code meets the required format
 * or criteria for validation.
 *
 * @returns {boolean} - Returns true if the land code is valid, otherwise false.
 */
function verifyLandCode() {
  landCode.value = landCode.value.trim()
  if (landCode.value.charAt(0) !== '+') {
    landCode.value = '+' + landCode.value
  } else {
    landCode.value = '+' + landCode.value.split('+').join('');
  }

  return stringVerificationUtils.verifyStringForNumbers(landCode.value.split('+')[1])
}


/**
 * Sets an error message to be displayed on the error label.
 *
 * @param {string} errorMsg - The error message to display.
 */
function setErrorLabel(errorMsg: string) {
  if (errorLabelEl.value) {
    errorLabelEl.value.textContent = "Registration failed: " + errorMsg
  }
  console.log("Registration failed: ", errorMsg);
}

/**
 * Handles the user registration process when the form is submitted.
 *
 * @param {Event} event - The event object triggered by the form submission.
 * @returns {Promise<void>} A promise that resolves when the registration process is complete.
 */
async function handleRegistration(event: Event) {
  // When handleRegistration is called, set all touched flag to true
  emailTouched.value = true
  firstNameTouched.value = true
  lastNameTouched.value = true
  landCodeTouched.value = true
  phoneNrTouched.value = true
  passwordTouched.value = true
  repeatPasswordTouched.value = true

  if (!validForm.value) {
    event.preventDefault()
    setErrorLabel("Form invalid")
    return
  }

  try {
    const formData: UserRegistrationDTO = {
    email: email.value,
    name: firstName.value,
    surname: lastName.value,
    phoneNumber: parseInt(phoneNr.value),
    countryCode: parseInt(landCode.value.split('+')[1]),
    password: password.value
    }

    emit("signup", formData)
  } catch (error) {
    setErrorLabel(String(error))
  }
}

// Expose for testing
defineExpose({
  email,
  firstName,
  lastName,
  landCode,
  phoneNr,
  password,
  repeatPassword,
  validForm,
  verifyLandCode,
  handleRegistration,
  errorLabelEl,
  emailTouched,
  firstNameTouched,
  lastNameTouched,
  landCodeTouched,
  phoneNrTouched,
  passwordTouched,
  repeatPasswordTouched
})
</script>

<template>
  <div class="registration-form" id="registration-form">
    <form @submit.prevent="handleRegistration">
      <label for="registration">Please fill out the fields below:</label>

      <input
        class="input"
        type="text"
        id="email"
        name="email"
        placeholder="Email"
        v-model="email"
        @blur="emailTouched = true"
        required
      />
      <p v-if="emailTouched && !validEmail" class="error-msg">Please enter a valid email.</p>

      <input
        class="input"
        type="text"
        id="firstName"
        name="firstName"
        placeholder="First Name"
        v-model="firstName"
        @blur="firstNameTouched = true"
        required
      />
      <p v-if="firstNameTouched && !validFirstName" class="error-msg">Only letters allowed.</p>

      <input
        class="input"
        type="text"
        id="lastName"
        name="lastName"
        placeholder="Last Name"
        v-model="lastName"
        @blur="lastNameTouched = true"
        required
      />
      <p v-if="lastNameTouched && !validLastName" class="error-msg">Only letters allowed.</p>

      <div class="phone-input-div">
        <input
          class="input"
          type="text"
          id="landCode"
          name="landCode"
          placeholder="+47"
          v-model="landCode"
          @blur="landCodeTouched = true"
          required
        />
        <input
          class="input"
          type="text"
          id="phoneNr"
          name="phoneNr"
          placeholder="Phone Number"
          v-model="phoneNr"
          @blur="phoneNrTouched = true"
          required
        />
      </div>
      <p v-if="(landCodeTouched && !validLandCode) || (phoneNrTouched && !validPhoneNumber)" class="error-msg">
        Please enter a valid phone number.
      </p>

      <input
        class="input"
        type="password"
        id="password"
        name="password"
        placeholder="Password"
        v-model="password"
        @blur="passwordTouched = true"
        required
      />
      <p v-if="passwordTouched && !validPassword" class="error-msg">Password cannot be empty.</p>

      <input
        class="input"
        type="password"
        id="repeatPassword"
        name="repeatPassword"
        placeholder="Repeat Password"
        v-model="repeatPassword"
        @blur="repeatPasswordTouched = true"
        required
      />
      <p v-if="repeatPasswordTouched && (password !== repeatPassword)" class="error-msg">
        Passwords do not match.
      </p>

      <input type="submit" :disabled="!validForm" value="Sign up" id="signup-button">
      <label for="error" id="registration-status-label" ref="errorLabelEl"></label>
    </form>
  </div>
</template>

<style>
@media ( min-width: 500px) {
  .registration-form {
  display: grid;
  grid-template-rows: 1fr;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-soft);
  width: 25%;
  min-width: 300px;
  }
}

.registration-form form {
  display: grid;
  grid-template-rows: auto;
  place-items: center;
  gap: 10px;
  width: 80%;
  min-width: 250px;
}


.phone-input-div {
  display: grid;
  grid-template-columns: 1fr 3fr;
  gap: 0;
  width: 80%;
}

.phone-input-div .input {
  width: 100%;
}

#signup-button {
  background-color: var(--color-primary);
  color: #FFFFFF;
  border-radius: var(--size-button-radius);
  padding: var(--size-button-padding);
  min-width: var(--size-button-min);
  align-self: center;
  width: auto;
}

#signup-button:hover {
  background-color: var(--color-primary-hover);
}

#signup-button:disabled {
  background-color: var(--color-background-mute);
  color: var(--color-button-disabled);
}

.input {
  width: 80%;
}

.error-msg {
  color: red;
  font-size: 0.9em;
  margin: 0;
}
</style>
