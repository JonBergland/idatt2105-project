<script setup lang="ts">
import LogIn from "../../components/LogIn/LogInForm.vue";
</script>

<template>
 <div class="login-wrapper">
  <h1>Log In</h1>
  <LogIn />
  <h4>or</h4>
  <button class="signup-button">Sign up</button>
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
</style>
