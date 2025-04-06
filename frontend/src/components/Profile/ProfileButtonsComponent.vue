<script setup lang="ts">
defineProps<{
  editMode: boolean,
  logoutMode: boolean
}>()

const emit = defineEmits(['editMode', 'logoutMode', 'save', 'logout', 'cancel'])
</script>

<template>
  <div v-if="!editMode && !logoutMode" class="profile-buttons">
    <p></p>
    <div class="buttons-container">
      <button class="edit-info-button" @click="emit('editMode')">Edit info</button>
      <button class="logout-button" @click="emit('logoutMode')">Log Out</button>
    </div>
  </div>

  <div v-else-if="editMode && !logoutMode" class="profile-buttons">
    <p></p>
    <div class="buttons-container">
      <button class="save-info" @click="emit('save')">Save</button>
      <button class="cancel-button" @click="emit('cancel')">Cancel</button>
    </div>
  </div>

  <div v-else-if="!editMode && logoutMode" class="profile-buttons">
    <p>Are you sure you want to sign out?</p>
    <div class="buttons-container">
      <button class="logout" @click="emit('logout')">Yes</button>
      <button class="cancel-button" @click="emit('cancel')">Cancel</button>
    </div>
  </div>

  <div v-else class="profile-buttons"></div>

</template>

<style>
.profile-buttons-container {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}
</style>
