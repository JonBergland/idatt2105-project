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
const userError = ref("")

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
  userError.value = "";

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

/**
 * Handles saving the user data.
 *
 * @param {User} user - The user object containing the data to be saved.
 * @returns {Promise<void>} A promise that resolves when the user data is successfully saved.
 */
async function handleSaveUser(user: User) {
  try {
    const userUpdated = await userStore.postUserInfo(user);
    if (userUpdated) {
      await loadUserData();
    } else {
      userError.value = "User was not updated";
    }
  } catch (error) {
    userError.value = "Error while updating user"
    console.log("Error while updating user: ", error);
  }
}

/**
 * Handles the user logout process.
 */
async function handleLogout() {
  try {
    await authStore.logout();
    router.push({ name: 'home' });
  } catch (error) {
    userError.value = "Error while logging out the user"
    console.log("Error while logging out the user: ", error);
  }
}

/**
 * Handles the 'item-clicked' event emitted by the ItemGroup component
 * @param {number} itemID - The unique identifier of the clicked item
 */
function handleItemClick(itemID: number) {
  router.push({ name: 'product', query: { id: itemID } });
}
</script>

<template>
<div class="user-profile-wrapper">
  <!-- Profile info -->
   <ProfileInfoComponent
   :user="userStore.user"
   :errorMessage="userError"
   @saveUser="handleSaveUser"
   @logoutUser="handleLogout"
   />

  <!-- User or Admin profile specific  -->
   <div v-if="userStore.user?.role === 'ROLE_USER'" class="profile-specifics">
    <h1>Your listings</h1>
    <div v-if="isLoading" class="loading">
      Loading your listings...
    </div>
    <div v-else-if="flattenedItems.length === 0" class="empty-state">
      <p>You don't have any listings yet.</p>
      <p>Create your first listing by clicking the "New listing" button in the navigation bar.</p>
    </div>
    <ItemGroup
      v-else
      :items="flattenedItems"
      mode="Grid"
      @item-clicked="handleItemClick"
    />
   </div>
</div>
</template>

<style scoped>
.user-profile-wrapper {
  padding: 8px;
}

.profile-specifics {
  display: flex;
  padding: 64px;
  gap: 16px;
  flex-direction: column;
  align-items: center;
}

.loading {
  margin-top: 10px;
  color: #6c757d;
  font-style: italic;
}

.empty-state {
  margin: 40px 0;
  padding: 20px;
  border-radius: 8px;
  background-color: var(--color-background-soft, #f8f9fa);
  max-width: 500px;
  text-align: center;
}

.empty-state p {
  margin: 10px 0;
  color: var(--color-text-secondary, #6c757d);
}

.empty-state p:first-child {
  font-size: 1.2rem;
  font-weight: 500;
  color: var(--color-text, #212529);
}
</style>
