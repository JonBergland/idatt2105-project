<script setup lang="ts">
import "@/assets/color.css"
import "@/assets/main.css"

defineProps<{
  editMode: boolean,
  logoutMode: boolean,
  activeSaveButton: boolean
  errorMessage: string
}>()

const emit = defineEmits(['editMode', 'logoutMode', 'save', 'logout', 'cancel'])
</script>

<template>
  <div v-if="!editMode && !logoutMode" class="profile-buttons">
    <p v-if="(errorMessage.trim().length > 0)" class="error-message">{{ errorMessage }}</p>
    <div class="buttons-container">
      <button class="edit-info-button" @click="emit('editMode')">Edit info</button>
      <button class="logout-button" @click="emit('logoutMode')">Log Out</button>
    </div>
  </div>

  <div v-else-if="editMode && !logoutMode" class="profile-buttons">
    <p v-if="(errorMessage.trim().length > 0)" class="error-message">{{ errorMessage }}</p>
    <div class="buttons-container">
      <button :disabled="!activeSaveButton" class="save-info" @click="emit('save')">Save</button>
      <button class="cancel-button" @click="emit('cancel')">Cancel</button>
    </div>
  </div>

  <div v-else-if="!editMode && logoutMode" class="profile-buttons">
    <p class="logout-confirmation">Are you sure you want to sign out?</p>
    <div class="buttons-container">
      <button class="logout" @click="emit('logout')">Yes</button>
      <button class="cancel-button" @click="emit('cancel')">Cancel</button>
    </div>
  </div>

  <div v-else class="profile-buttons"></div>
</template>

<style scoped>
.profile-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.profile-buttons p {
  height: 15px;
  text-align: center;
}

.profile-buttons .error-message {
  color: var(--color-danger-muted);
  margin-bottom: 5px;
}

.profile-buttons .logout-confirmation {
  font-weight: 500;
  height: auto;
  margin-bottom: 10px;
}

.profile-buttons button {
  width: 83px;
}

.buttons-container {
  display: flex;
  gap: 20px;
  margin-top: 10px;
  justify-content: center;
}

.edit-info-button {
  background-color: var(--color-primary);
  color: #FFFFFF;
}

.edit-info-button:hover {
  background-color: var(--color-primary-hover);
}

.logout-button,
.cancel-button {
  background-color: var(--color-background-mute);
  color: var(--color-button-disabled);
  border-color: var(--color-border);
}

.logout-button:hover,
.cancel-button:hover {
  background-color: var(--color-background-soft)
}

.save-info {
  background-color: var(--color-primary);
  color: white;
}

.save-info:hover {
  background-color: var(--color-primary-hover);
}

.save-info:disabled {
  background-color: var(--color-background-mute);
  color: var(--color-button-disabled);
}

.logout {
  background-color: var(--color-danger-muted);
  color: white;
}

.logout:hover {
  background-color: var(--color-danger-muted-hover);
}
</style>
