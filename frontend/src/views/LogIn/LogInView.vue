<script setup lang="ts">
import LogIn from "../../components/LogIn/LogInForm.vue";
import { useRouter } from 'vue-router';

const router = useRouter()

/**
 * Navigates the user to the "sign-up" route when the sign-up button is clicked.
 */
const handleSignUpButton = () => {
  console.log("Push to route sign-up");
  router.push({ name: 'signup' });
};
</script>

<template>
 <div class="login-wrapper">
  <h1>Log In</h1>
  <LogIn />
  <h4>or</h4>
  <button class="signup-button" @click="handleSignUpButton">Sign up</button>
 </div>

</template>

<style>
.login-wrapper {
  display: grid;
  grid-template-columns: auto;
  place-items: center;
  color: var(--color-heading);
  gap: 5px;
}

.signup-button {
  background-color: var(--color-secondary);
  width: auto;
}

.signup-button:hover {
  background-color: var(--color-secondary-hover);
}
</style>
