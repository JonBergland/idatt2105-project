<script setup lang="ts">
import profileImage from '@/assets/icons/profile-avatar.svg'
import type { User } from '@/models/user';
import { ref } from 'vue';
import UserInfoComponent from '@/components/Profile/UserInfoComponent.vue';
import ProfileButtonsComponent from '@/components/Profile/ProfileButtonsComponent.vue';

defineProps<{
  user: User | null
}>()

const emit = defineEmits(["saveUser", "logoutUser"])

const isEditing = ref(false)
const isLoggingOut = ref(false)

function handleEditMode() {
  isEditing.value = true
  isLoggingOut.value = false
}

function handleLogoutMode() {
  isEditing.value = false
  isLoggingOut.value = true
}

function handleSave() {
  isEditing.value = false
  isLoggingOut.value = false

  // TODO: add logic for updating user info
}

function handleLogout() {
  isEditing.value = false
  isLoggingOut.value = false

  emit("logout")
}

function handleCancel() {
  isEditing.value = false
  isLoggingOut.value = false
}

</script>
<template>
  <div class="profile-info-wrapper">
    <div class="profile-image">
      <img :src="profileImage" alt="profileImage" />
    </div>
    <div class="profile-details">
      <UserInfoComponent
        :first-name="user?.name"
        :last-name="user?.surname"
        :email="user?.email"
        :phoneNumber="'+' + user?.countryCode + ' ' + user?.phoneNumber"
        :location="user?.city"
      />
      <ProfileButtonsComponent
      :editMode = isEditing
      :logoutMode = isLoggingOut
      @edit-mode="handleEditMode"
      @logout-mode="handleLogoutMode"
      @save="handleSave"
      @logout="handleLogout"
      @cancel="handleCancel"
      />
    </div>
  </div>
</template>

<style scoped>
.profile-info-wrapper {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  margin-top: 20px;
}
</style>
