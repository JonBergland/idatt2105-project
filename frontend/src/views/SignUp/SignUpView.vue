<script setup lang="ts">
import SignUpForm from '@/components/SignUp/SignUpForm.vue';
import type { UserRegistrationDTO } from '@/models/user';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore()

async function handleSignUp(registrationForm: UserRegistrationDTO) {
  try {
    console.log("Handing signup");

    const resp = await authStore.signup(registrationForm)

    //TODO: handle boolean response from signup
    console.log(resp);


  } catch (error) {
    console.log("Error in handling singup in SignupView: ", error);
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
