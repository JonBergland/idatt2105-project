<script setup lang="ts">
import ItemGroup from "@/components/Home/ItemGroup.vue";
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { ItemsResponseDTO } from "@/models/item";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { useUserStore } from "@/stores/userStore";
import { onMounted, ref, watch } from "vue";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter();
const user = ref<User | null>(null);
const userItems = ref<ItemsResponseDTO | null>(null);

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' })
        return;
      }
      userItems.value = await userStore.getUserItems();
      console.log("Items gathered from the user: ", userItems.value);

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
   <div v-if="user?.role === 'ROLE_USER' && userItems?.items" class="profile-specifics">
    <h1>Your listings: </h1>
    <ItemGroup
    :items="userItems.items"
    mode="Grid"
    />
   </div>
</div>
</template>

<style scoped>

.profile-specifics {
  margin-top: 20px;
  padding: 10px;
  border-top: 2px solid black;
}
</style>
