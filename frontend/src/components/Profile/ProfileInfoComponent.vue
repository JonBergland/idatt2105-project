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
const editableUser = ref(user)

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

  emit("logoutUser")
}

function handleCancel() {
  isEditing.value = false
  isLoggingOut.value = false

  editableUser.value = user
}

</script>
<template>
  <div class="profile-info-wrapper">
    <div class="profile-image">
      <img id="profile-image" :src="profileImage" alt="profileImage" />
    </div>
    <div class="profile-details">
      <UserInfoComponent
        v-model:firstName="editableUser.firstName"
        v-model:lastName="editableUser.lastName"
        v-model:email="editableUser.email"
        v-model:phoneNumber="editableUser.phoneNumber"
        v-model:location="editableUser.location"
        :isEditing="isEditing"
        @save="handleSave"
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
  flex-wrap: wrap;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  margin-top: 20px;
}

.profile-image {
  flex: 1;
  display: flex;
  justify-content: right;
  align-items: center;
  max-width: 50%;
  margin-right: 50px;
  margin-top: 25px;
}

.profile-image img {
  width: 100%;
  max-width: 275px;
  height: auto;
  object-fit: contain;
}

.profile-details {
  flex: 1;
  display: flex;
  flex-direction: row;
  max-width: 50%;
}

#user-info-component {
  margin-top: 25px;
}

@media (max-width: 768px) {
  .profile-info-wrapper {
    flex-direction: column;
    align-items: center;
  }

  .profile-image {
    max-width: 100%;
    justify-content: center;
    margin-right: 0;
    margin-bottom: 20px;
  }

  .profile-image img {
    max-width: 180px;
  }

  .profile-details {
    max-width: 100%;
    flex-direction: column;
    align-items: center;
  }
}

@media (max-width: 480px) {
  .profile-image img {
    max-width: 140px;
  }
}
</style>
