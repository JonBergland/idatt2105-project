<script setup lang="ts">
import ItemGroup from "@/components/Home/ItemGroup.vue";
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { ItemResponseDTO } from "@/models/item";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { useUserStore } from "@/stores/userStore";
import { computed, onMounted } from "vue";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter();

const flattenedItems = computed((): ItemResponseDTO[] => {
  return (userStore.userItems.items).map(item => item);
});

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' })
        return;
      }
      await userStore.updateUserItems();
      console.log("Items gathered from the user: ", userStore.userItems);

    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push({ name: 'login' })
      return;
    }
  }
});

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
   :user = userStore.user
   @saveUser="handleSaveUser"
   @logoutUser="handleLogout"
   />

  <!-- User or Admin profile specific  -->
   <div v-if="userStore.user?.role === 'ROLE_USER'" class="profile-specifics">
    <h1>Your listings: </h1>
    <ItemGroup
    :items="flattenedItems"
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
