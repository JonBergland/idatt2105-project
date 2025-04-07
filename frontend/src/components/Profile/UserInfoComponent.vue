<script setup lang="ts">
import { ref, computed, watch } from "vue";
import * as stringVerificationUtils from "@/utils/stringVerificationUtils";

const props = defineProps<{
  firstName: string | undefined,
  lastName: string | undefined,
  email: string | undefined,
  countryCode: number | undefined,
  phoneNumber: number | undefined,
  address: string | undefined,
  postalCode: number | undefined,
  city: string | undefined,
  isEditing: boolean
}>();

const localFirstName = ref(props.firstName || "");
const localLastName = ref(props.lastName || "");
const localEmail = ref(props.email || "");
const localCountryCode = ref(String(props.countryCode || ""));
const localPhoneNumber = ref(String(props.phoneNumber || ""));
const localAddress = ref(props.address || "");
const localPostalCode = ref(String(props.postalCode || ""));
const localCity = ref(props.city || "");

/**
 * Formats a given country code for the phone number.
 *
 * @param {string} code - The country code to format (e.g., "+1", "+46").
 * @returns {string} - The formatted country code.
 */
function formatCountryCode(code: string): string {
  code = code.trim();
  if (code.charAt(0) !== "+") {
    code = "+" + code;
  } else {
    code = "+" + code.split("+").join("");
  }
  return code;
}

const validFirstName = computed(() =>
  stringVerificationUtils.verifyStringForLetters(localFirstName.value));
const validLastName = computed(() =>
  stringVerificationUtils.verifyStringForLetters(localLastName.value));
const validEmail = computed(() =>
  stringVerificationUtils.verifyStringForEmail(localEmail.value));
const validCountryCode = computed(() => {
  localCountryCode.value = formatCountryCode(localCountryCode.value)
  return stringVerificationUtils.verifyStringForNumbers(localCountryCode.value.split("+")[1] || "");
});
const validPhoneNumber = computed(() =>
  stringVerificationUtils.verifyStringForNumbers(localPhoneNumber.value));
const validAddress = computed(() =>
  stringVerificationUtils.verifyStringNotEmpty(localAddress.value));
const validPostalCode = computed(() =>
  stringVerificationUtils.verifyStringForNumbers(localPostalCode.value));
const validCity = computed(() =>
  stringVerificationUtils.verifyStringNotEmpty(localCity.value));

const validForm = computed(() => {
  return validFirstName.value &&
    validLastName.value &&
    validEmail.value &&
    validCountryCode.value &&
    validPhoneNumber.value &&
    validAddress.value &&
    validPostalCode.value &&
    validCity.value;
});

const emit = defineEmits([
  "update:firstName",
  "update:lastName",
  "update:email",
  "update:countryCode",
  "update:phoneNumber",
  "update:address",
  "update:postalCode",
  "update:city",
  "update"
]);


/**
 * Handles the update operation for the user information.
 * This function is triggered when the user initiates an update action.
 * It is responsible for processing and applying the changes to the user's profile data.
 */
function handleUpdate() {
  if (validForm.value) {
    emit("update:firstName", localFirstName.value);
    emit("update:lastName", localLastName.value);
    emit("update:email", localEmail.value);
    emit("update:countryCode", parseInt(localCountryCode.value.replace("+", "")));
    emit("update:phoneNumber", parseInt(localPhoneNumber.value));
    emit("update:address", localAddress.value);
    emit("update:postalCode", parseInt(localPostalCode.value));
    emit("update:city", localCity.value);
  }
  emit("update", validForm.value)
}

watch(() => props.firstName, (newVal) => localFirstName.value = newVal || "");
watch(() => props.lastName, (newVal) => localLastName.value = newVal || "");
watch(() => props.email, (newVal) => localEmail.value = newVal || "");
watch(() => props.countryCode, (newVal) => localCountryCode.value = String(newVal || ""));
watch(() => props.phoneNumber, (newVal) => localPhoneNumber.value = String(newVal || ""));
watch(() => props.address, (newVal) => localAddress.value = newVal || "");
watch(() => props.postalCode, (newVal) => localPostalCode.value = String(newVal || ""));
watch(() => props.city, (newVal) => localCity.value = newVal || "");

defineExpose({
  validForm
})
</script>

<template>
  <div v-if="!isEditing" class="user-info-container">
    <h1>{{ firstName + " " + lastName }}</h1>
    <h3>{{ "First name: " + firstName }}</h3>
    <h3>{{ "Last name: " + lastName }}</h3>
    <h3>{{ "Email: " + email }}</h3>
    <h3>{{ "Phone number: +" + countryCode + " " + phoneNumber }}</h3>
    <h3>{{ "Address: " + address }}</h3>
    <h3>{{ "Postal Code: " + postalCode }}</h3>
    <h3>{{ "Location: " + city }}</h3>
  </div>
  <div v-else-if="isEditing" class="user-info-container">
    <form>
      <div class="form-group">
        <label for="firstName">First Name</label>
        <input
          type="text"
          id="firstName"
          class="form-input"
          v-model="localFirstName"
          @input="handleUpdate"
          required
        />
        <p v-if="!validFirstName" class="error-msg">Only letters allowed.</p>
      </div>

      <div class="form-group">
        <label for="lastName">Last Name</label>
        <input
          type="text"
          id="lastName"
          class="form-input"
          v-model="localLastName"
          @input="handleUpdate"
          required
        />
        <p v-if="!validLastName" class="error-msg">Only letters allowed.</p>
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          class="form-input"
          v-model="localEmail"
          @input="handleUpdate"
          required
        />
        <p v-if="!validEmail" class="error-msg">Please enter a valid email.</p>
      </div>

      <div class="form-group">
        <label for="phoneNumber">Phone Number</label>
        <div class="phone-group">
          <input
            type="tel"
            id="countryCode"
            class="form-input"
            v-model="localCountryCode"
            @input="handleUpdate"
            required
          />
          <input
            type="tel"
            id="phoneNumber"
            class="form-input"
            v-model="localPhoneNumber"
            @input="handleUpdate"
            required
          />
        </div>
        <p v-if="!validCountryCode || !validPhoneNumber" class="error-msg">
          Please enter a valid phone number.
        </p>
      </div>

      <div class="form-group">
        <label for="address">Address</label>
        <input
          type="text"
          id="address"
          class="form-input"
          v-model="localAddress"
          @input="handleUpdate"
          required
        />
        <p v-if="!validAddress" class="error-msg">Address cannot be empty.</p>
      </div>

      <div class="form-group">
        <label for="postalCode">Postal Code</label>
        <input
          type="text"
          id="postalCode"
          class="form-input"
          v-model="localPostalCode"
          @input="handleUpdate"
          required
        />
        <p v-if="!validPostalCode" class="error-msg">Please enter a valid postal code.</p>
      </div>

      <div class="form-group">
        <label for="city">City</label>
        <input
          type="text"
          id="city"
          class="form-input"
          v-model="localCity"
          @input="handleUpdate"
          required
        />
        <p v-if="!validCity" class="error-msg">City cannot be empty.</p>
      </div>
    </form>
  </div>
  <div v-else></div>
</template>

<style scoped>
.user-info-container {
  display: flex;
  flex-direction: column;
  align-items: left;
  gap: 20px;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
  width: 100%;
}

.form-group label {
  font-weight: 500;
  margin-bottom: 5px;
  color: var(--color-heading);
}

.form-input {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  width: 100%;
  font-size: 16px;
}

.form-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px rgba(13, 94, 1, 0.1);
}

.phone-group {
  display: grid;
  grid-template-columns: 1fr 3fr;
  gap: 0;
}
</style>


