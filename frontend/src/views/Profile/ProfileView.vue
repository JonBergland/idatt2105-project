<script setup lang="ts">
import ProfileInfoComponent from "@/components/Profile/ProfileInfoComponent.vue";
import type { User } from "@/models/user";
import { useAuthStore } from "@/stores/authStore";
import { onMounted, ref } from "vue";

const authStore = useAuthStore()
const user = ref<User | null>(null);

onMounted(() => {
  if (authStore.isAuth) {
    user.value = authStore.userData;
  }
});

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
