<script setup lang="ts">
import { ref, computed, watch } from "vue";
import * as stringVerificationUtils from "@/utils/stringVerificationUtils";

const props = defineProps<{
  firstName: string | undefined,
  lastName: string | undefined,
  email: string | undefined,
  countryCode: number | undefined,
  phoneNumber: number | undefined,
  location: string | undefined,
  isEditing: boolean
}>();

const localFirstName = ref(props.firstName || "");
const localLastName = ref(props.lastName || "");
const localEmail = ref(props.email || "");
const localCountryCode = ref(String(props.countryCode || ""));
const localPhoneNumber = ref(String(props.phoneNumber || ""));
const localLocation = ref(props.location || "");

const validFirstName = computed(() =>
  stringVerificationUtils.verifyStringForLetters(localFirstName.value));
const validLastName = computed(() =>
  stringVerificationUtils.verifyStringForLetters(localLastName.value));
const validEmail = computed(() =>
  stringVerificationUtils.verifyStringForEmail(localEmail.value));
const validCountryCode = computed(() => {
  let code = localCountryCode.value.trim();
  if (code.charAt(0) !== "+") {
    code = "+" + code;
  } else {
    code = "+" + code.split("+").join("");
  }
  localCountryCode.value = code;
  return stringVerificationUtils.verifyStringForNumbers(code.split("+")[1] || "");
});
const validPhoneNumber = computed(() =>
  stringVerificationUtils.verifyStringForNumbers(localPhoneNumber.value));
const validLocation = computed(() =>
  stringVerificationUtils.verifyStringNotEmpty(localLocation.value));

const validForm = computed(() => {
  return validFirstName.value &&
    validLastName.value &&
    validEmail.value &&
    validCountryCode.value &&
    validPhoneNumber.value &&
    validLocation.value;
});

// Emit events for two-way binding and save
const emit = defineEmits([
  "update:firstName",
  "update:lastName",
  "update:email",
  "update:countryCode",
  "update:phoneNumber",
  "update:location",
  "update"
]);

function handleUpdate() {
  if (validForm.value) {
    emit("update:firstName", localFirstName.value);
    emit("update:lastName", localLastName.value);
    emit("update:email", localEmail.value);
    emit("update:countryCode", parseInt(localCountryCode.value.replace("+", "")));
    emit("update:phoneNumber", parseInt(localPhoneNumber.value));
    emit("update:location", localLocation.value);
  }
  emit("update", validForm.value)
}

watch(() => props.firstName, (newVal) => localFirstName.value = newVal || "");
watch(() => props.lastName, (newVal) => localLastName.value = newVal || "");
watch(() => props.email, (newVal) => localEmail.value = newVal || "");
watch(() => props.countryCode, (newVal) => localCountryCode.value = String(newVal || ""));
watch(() => props.phoneNumber, (newVal) => localPhoneNumber.value = String(newVal || ""));
watch(() => props.location, (newVal) => localLocation.value = newVal || "");
</script>

<template>
  <div v-if="!isEditing" class="user-info-container">
    <h1>{{ firstName + " " + lastName }}</h1>
    <h3>{{ "First name: " + firstName }}</h3>
    <h3>{{ "Last name: " + lastName }}</h3>
    <h3>{{ "Email: " + email }}</h3>
    <h3>{{ "Phone number: +" + countryCode + " " + phoneNumber }}</h3>
    <h3>{{ "Location: " + location }}</h3>
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
        <label for="location">Location</label>
        <input
          type="text"
          id="location"
          class="form-input"
          v-model="localLocation"
          @input="handleUpdate"
          required
        />
        <p v-if="!validLocation" class="error-msg">Location cannot be empty.</p>
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


