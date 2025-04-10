<script setup lang="ts">
import SignUpForm from '@/components/SignUp/SignUpForm.vue';
import type { UserRegistrationDTO } from '@/models/user';
import { useAuthStore } from '@/stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore()
const router = useRouter()

/**
 * Handles the user sign-up process.
 *
 * @param {UserRegistrationDTO} registrationForm - A {@link UserRegistrationDTO} containing
 * the user's registration information, such as username, password, and other required details.
 *
 * @returns {Promise<void>} A promise that resolves when the sign-up process is complete.
 */
async function handleSignUp(registrationForm: UserRegistrationDTO) {
  try {
    console.log("Handing signup");
    const resp = await authStore.signup(registrationForm)

    if(resp) {
      router.push({ name: 'home' })
    } else {
      throw new Error("Response from store was false");
    }
  } catch (error) {
    console.log("Error in handling signup in SignupView: ", error);
  }
}
</script>

<template>
<div class="signup-wrapper">
  <h1>Welcome to the Yard!</h1>
  <SignUpForm @signup="handleSignUp" />
</div>
</template>

<style>
.signup-wrapper {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto;
  place-items: center;
  color: var(--color-heading);
  gap: 20px;
  margin-top: 10px;
}
</style>
