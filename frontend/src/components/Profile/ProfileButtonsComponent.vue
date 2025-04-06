<script setup lang="ts">
defineProps<{
  editMode: boolean,
  logoutMode: boolean
}>()

const emit = defineEmits(['editMode', 'logoutMode', 'save', 'logout', 'cancel'])
</script>

<template>
  <div v-if="!editMode && !logoutMode" class="profile-buttons-container">
        <button class="edit-info-button">Edit info</button>
        <button class="logout-button">Log Out</button>
  </div>

  <div v-else-if="editMode && !logoutMode" class="profile-buttons-container">
        <button class="save-info">Save</button>
        <button class="cancel-button"> Cancel </button>
  </div>

  <div v-else-if="!editMode && logoutMode" class="profile-buttons-container">
        <button class="logout">Logout</button>
        <button class="cancel-button"> Cancel </button>
  </div>

  <div v-else class="profile-buttons-container"></div>

</template>

<style>
.profile-buttons-container {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}
</style>
