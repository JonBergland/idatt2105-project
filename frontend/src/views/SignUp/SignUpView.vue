<script setup lang="ts">
import SignUpForm from '@/components/SignUp/SignUpForm.vue';

</script>

<template>
<div class="signup-wrapper">
  <h1>Welcome to the Yard!</h1>
  <SignUpForm />
</div>
</template>

<style>
.signup-wrapper {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto;
  place-items: center;
  color: var(--color-heading);
  gap: 5px;
}
</style>
