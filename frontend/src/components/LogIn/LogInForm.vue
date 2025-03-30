<script setup lang="ts">
import "@/assets/color.css"
import "@/assets/base.css"
</script>

<template>
  <div class="login-form" id="login-form">
    <form action="">
      <label for="login">Please enter email and password:</label>
      <input class="input" type="text" id="email" name="email" placeholder="Email" required />
      <input class="input" type="text" id="password" name="password" placeholder="Password" required/>
      <input type="submit" value="Log in" id="login-button">
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
</style>
