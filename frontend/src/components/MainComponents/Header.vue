<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const pageName = computed(() => route.name);

const isListingPage = computed(() => pageName.value === "home");
const isFavoritesPage = computed(() => pageName.value === "favorites");
const isMessagePage = computed(() => pageName.value === "messages");
const isProfilePage = computed(() => pageName.value === "profile");
const isLoginPage = computed(() => pageName.value === "login")
const isSignupPage = computed(() => pageName.value === "signup")

//TODO: implement logic for notifications
const hasNotifications = ref(true)

//TODO: implement logic for token
const hasToken = ref(false)

</script>

<template>
  <header>
    <router-link to="/">
     <img src="@/assets/logos/logo.svg" alt="Yard logo" id="desktopLogo" />
     <img src="@/assets/logos/logo3.svg" alt="Yard logo mobile" id="mobileLogo" />
    </router-link>
    <div class="buttonWrapper" v-if="!isLoginPage && !isSignupPage && hasToken">
      <router-link
      to="/listing"
      class="routerLink"
      :class="{ active: isListingPage }"
      >
        <img
          v-if="isListingPage"
          src="@/assets/icons/tag-selected.svg"
          alt="Tag"
          class="icon"
        />
        <img
          v-else
          src="@/assets/icons/tag.svg"
          alt="Tag"
          class="icon"
        />
      <p>New listing</p>
      </router-link>

      <router-link
        to="/home"
        class="routerLink"
        :class="{ active: isFavoritesPage }"
      >
        <img
          v-if="isFavoritesPage"
          src="@/assets/icons/heart-selected.svg"
          alt="Heart"
          class="icon"
        />
        <img
          v-else
          src="@/assets/icons/heart.svg"
          alt="Heart"
          class="icon"
        />
        <p>Favorites</p>
      </router-link>

      <router-link
        to="/messages"
        class="routerLink"
        :class="{ active: isMessagePage }"
      >
        <img
          v-if="isMessagePage && hasNotifications"
          src="@/assets/icons/message-notification-selected.svg"
          alt="Message"
          class="icon"
        />
        <img
          v-else-if="isMessagePage && !hasNotifications"
          src="@/assets/icons/message-selected.svg"
          alt="Message"
          class="icon"
        />
        <img
          v-else-if="!isMessagePage && hasNotifications"
          src="@/assets/icons/message-notification.svg"
          alt="Message"
          class="icon"
        />
        <img
          v-else
          src="@/assets/icons/message.svg"
          alt="Message"
          class="icon"
        />
        <p>Messages</p>
      </router-link>

      <router-link
        to="/profile"
        class="routerLink"
        :class="{ active: isProfilePage }"
      >
        <img
          v-if="isProfilePage"
          src="@/assets/icons/user-selected.svg"
          alt="User"
          class="icon"
        />
        <img
          v-else
          src="@/assets/icons/user.svg"
          alt="User"
          class="icon"
        />
        <p>Profile</p>
      </router-link>
    </div>

    <div v-else-if="!isLoginPage && !isSignupPage" class="buttonWrapper">
      <router-link
       to="/login"
       class="routerButton loginButton"
      >
      <p>Log in</p>
      </router-link>

      <router-link
       to="/singup"
       class="routerButton signupButton"
      >
      <p>Sign up</p>
      </router-link>
    </div>

    <div v-else class="buttonWrapper">
    </div>
  </header>
</template>

<style scoped>
header {
  display: flex;
  flex-direction: row;
  padding: 32px;
  height: 98px;
}

.routerLink {
  display: flex;
  flex-direction: row;
  gap: 10px;
  max-height: 30px;
  text-decoration: none;
  color: inherit;
  position: relative;
}

.routerLink:hover::after,
.routerLink.active::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: black;
}

.icon {
  max-height: 30px;
}

.buttonWrapper {
  display: flex;
  justify-content: flex-end;
  gap: 32px;
  display: flex;
  align-items: center;
  align-content: center;
  flex: 1 0 0;
  flex-wrap: wrap;
}

.routerButton {
  display: flex;
  width: 83px;
  padding: 4px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 8px;
  font-family: 'Inter', sans-serif;
  font-weight: 400;
  text-decoration: none;
  color: black;
  border: 2px solid gray;
  transition: all 0.3s ease;
}


.loginButton {
  color: black;
  border-color: gray;
  background-color: #e0e0e0;
}

.signupButton {
  color: white;
  border-color: #000000;
  background-color: #0D5E01;
}

.signupButton:hover {
  background-color: #0D5E01;
}

.routerLink p {
    font-family: 'Inter', sans-serif;
    font-weight: 400;
  }

#desktopLogo {
  display: block;
}

#mobileLogo {
  display: none;
}

@media (max-width: 768px) {
  #desktopLogo {
    display: none;
  }

  #mobileLogo {
    display: block;
  }

  .routerLink p {
    display: none;
  }

  .routerLink {
    width: 34px;
  }
}
</style>
