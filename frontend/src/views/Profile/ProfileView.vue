<script setup lang="ts">
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { onMounted, ref, watch } from "vue";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();
const user = ref<User | null>(null);

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push('/login');
        return;
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push('/login');
      return;
    }
  }

    // Set user data from auth store
    user.value = authStore.userData;
});

watch(
  () => authStore.userData,
  (newUserData) => {
    user.value = newUserData;
  },
  { deep: true }
);
</script>

<template>
<div class="user-profile-wrapper">
  <!-- Profile info -->
   <ProfileInfoComponent
   :user = user
   />


  <!-- User or Admin profile specific  -->
</div>

</template>

<script>

</script>
