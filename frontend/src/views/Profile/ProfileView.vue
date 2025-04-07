<script setup lang="ts">
import ItemGroup from "@/components/Home/ItemGroup.vue";
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { ItemResponseDTO } from "@/models/item";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { useUserStore } from "@/stores/userStore";
import { computed, onMounted, ref } from "vue";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter();
const isLoading = ref(true);

const flattenedItems = computed((): ItemResponseDTO[] => {
  return (userStore.userItems?.items || []).map(item => item);
});


/**
 * Asynchronously loads user data and items from the server.
 *
 * @returns {Promise<void>} A promise that resolves when the user data is successfully loaded.
 */
async function loadUserData() {
  isLoading.value = true;

  try {
    if (!authStore.isAuth) {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' });
        return;
      }
    }

    await userStore.updateUserItems();
    console.log("Items gathered from the user: ", userStore.userItems);
  } catch (error) {
    console.error("Error in profile page:", error);
    router.push({ name: 'login' });
  } finally {
    isLoading.value = false;
  }
}

onMounted(loadUserData);

async function handleSaveUser(user: User) {
  await userStore.postUserInfo(user);
  await loadUserData();
}

async function handleLogout() {
  await authStore.logout();
  router.push({ name: 'home' });
}
</script>

<template>
<div class="user-profile-wrapper">
  <!-- Profile info -->
   <ProfileInfoComponent
   :user="userStore.user"
   @saveUser="handleSaveUser"
   @logoutUser="handleLogout"
   />

  <!-- User or Admin profile specific  -->
   <div v-if="userStore.user?.role === 'ROLE_USER'" class="profile-specifics">
    <h1>Your listings:</h1>
    <div v-if="isLoading" class="loading">
      Loading your listings...
    </div>
    <div v-else-if="flattenedItems.length === 0" class="no-items">
      You don't have any listings yet.
    </div>
    <ItemGroup
      v-else
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

.loading, .no-items {
  padding: 20px;
  text-align: center;
  color: #666;
  font-style: italic;
}
</style>
