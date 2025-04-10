<script setup lang="ts">
import type { UserLoginDTO } from "@/models/user";
import LogIn from "../../components/LogIn/LogInForm.vue";
import { useRouter } from 'vue-router';
import { useAuthStore } from "@/stores/authStore";

const router = useRouter()
const authStore = useAuthStore()

/**
 * Navigates the user to the "sign-up" route when the sign-up button is clicked.
 */
function handleSignUpButton() {
  console.log("Push to route sign-up");
  router.push({ name: 'signup' });
};


/**
 * Handles the login process for a user.
 *
 * @param {UserLoginDTO} user   An object containing the user's login credentials.
 * @returns {Promise<void>}     A promise that resolves when the login process is complete.
 */
async function handleLogin(user: UserLoginDTO): Promise<void> {
  try {
    console.log("Handling login");
    const resp = await authStore.login(user)

    if(resp) {
      router.push({ name: 'home' })
    } else {
      throw new Error("Response from store was false");
    }
  } catch (error) {
    console.log("Error in handling login in LogInView: ", error);
  }
}

</script>
<template>
 <div class="login-wrapper">
  <h1>Log In</h1>
  <LogIn @login="handleLogin" />
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
  gap: 20px;
  margin-top: 10px;
}

.signup-button {
  background-color: var(--color-secondary);
  width: auto;
}

.signup-button:hover {
  background-color: var(--color-secondary-hover);
}
</style>
