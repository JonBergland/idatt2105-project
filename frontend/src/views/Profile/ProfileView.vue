<script setup lang="ts">
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { useUserStore } from "@/stores/userStore";
import { onMounted, ref, watch } from "vue";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter();
const user = ref<User | null>(null);

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' })
        return;
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push({ name: 'login' })
      return;
    }
  }

    // Set user data from auth store
    user.value = authStore.userData
});

watch(
  () => authStore.userData,
  (newUserData) => {
    user.value = newUserData
  },
  { deep: true }
);

async function handleSaveUser(user: User) {
  await userStore.postUserInfo(user)
}

async function handleLogout() {
  await authStore.logout()
  router.push({ name: 'home' })
}
</script>

<template>
<div class="user-profile-wrapper">
  <!-- Profile info -->
   <ProfileInfoComponent
   :user = user
   @saveUser="handleSaveUser"
   @logoutUser="handleLogout"
   />


  <!-- User or Admin profile specific  -->
</div>

</template>

<script>

</script>
