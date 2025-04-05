<script setup lang="ts">
import profileImage from '@/assets/icons/profile-avatar.svg'
</script>
<template>
  <div class="profile-info-wrapper">
    <div class="profile-image">
      <img :src="profileImage" alt="profileImage" />
    </div>
    <div class="profile-details">
      <div class="profile-info"></div>
      <div class="profile-buttons">
        <button class="edit-info-button"></button>
        <button class="logout-button"> </button> <!-- Add a popup-window for logout?  -->
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-info-wrapper {
}
</style>
