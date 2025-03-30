<script setup lang="ts">
import { ref } from 'vue'
import "@/assets/color.css"
import "@/assets/base.css"

const email = ref("")
const password = ref("")

async function handleLogin() {
  const formData = {
    email: email.value,
    password: password.value
  }

  try {
    // TODO: Add form submission logic
    console.log(formData);

  } catch (error) {
    const errorLabel = document.querySelector('label[id=login-status-label]')
    if (errorLabel != null) errorLabel.textContent = "Log in failed"
    console.log("Error when logging in: ", error);
  }
}

</script>

<template>
  <div class="login-form" id="login-form">
    <form @submit.prevent="handleLogin">
      <label for="login">Please enter email and password:</label>
      <input class="input" type="text" id="email" name="email" placeholder="Email" v-model="email" required />
      <input class="input" type="text" id="password" name="password" placeholder="Password" v-model="password" required/>
      <input type="submit" value="Log in" id="login-button">
      <label for="error" id="login-status-label"></label>
    </form>
  </div>
</template>

<style>

.login-form {
  display: grid;
  grid-template-columns: auto;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-soft);
}

.login-form form {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
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

</style>
