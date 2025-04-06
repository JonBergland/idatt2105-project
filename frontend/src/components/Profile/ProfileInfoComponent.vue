<script setup lang="ts">
import profileImage from '@/assets/icons/profile-avatar.svg'
import type { User } from '@/models/user';
import { ref, watch, onMounted } from 'vue';
import UserInfoComponent from '@/components/Profile/UserInfoComponent.vue';
import ProfileButtonsComponent from '@/components/Profile/ProfileButtonsComponent.vue';

const props = defineProps<{
  user: User | null
}>()

const emit = defineEmits(["saveUser", "logoutUser"])

const editableUser = ref<User | null>(null)
const validUser = ref(false)

watch(() => props.user, (newUser) => {
  if (newUser) {
    editableUser.value = {...newUser};
  }
}, { immediate: true });

const isEditing = ref(false)
const isLoggingOut = ref(false)

 /**
 * Toggles the edit mode for the profile information component.
 * Enables editing of user profile details while disabling logout mode.
 */
function handleEditMode() {
  isEditing.value = true
  isLoggingOut.value = false
}

/**
 * Toggles the logout mode for the profile information component.
 * Enables logout mode while disabling editing of user profile details.
 */
function handleLogoutMode() {
  isEditing.value = false
  isLoggingOut.value = true
}


/**
 * Saves the current state of the editable user profile.
 * Disables both editing and logout modes, and emits a "saveUser" event
 * with a cleaned user object.
 */
function handleSave() {
  isEditing.value = false
  isLoggingOut.value = false

  const cleanUser: User = {
    userID: editableUser.value.userID,
    name: editableUser.value.name,
    surname: editableUser.value.surname,
    email: editableUser.value.email,
    phoneNumber: editableUser.value.phoneNumber,
    countryCode: editableUser.value.countryCode,
    city: editableUser.value.city,
    role: editableUser.value.role,
    latitude: editableUser.value.latitude,
    longitude: editableUser.value.longitude,
    postalCode: editableUser.value.postalCode,
    address: editableUser.value.address
  }
  emit("saveUser", cleanUser)
}

/**
 * Handles the logout action for the user.
 * Disables both editing and logout modes, and emits a "logoutUser" event.
 */
function handleLogout() {
  isEditing.value = false
  isLoggingOut.value = false

  emit("logoutUser")
}

/**
 * Cancels any changes made to the editable user profile.
 * Resets the editable user to the original user data from props
 * and disables both editing and logout modes.
 */
function handleCancel() {
  isEditing.value = false
  isLoggingOut.value = false

  editableUser.value = props.user
}

/**
 * Updates the validity state of the user form.
 * Sets the `validUser` value based on the provided form validity.
 *
 * @param {boolean} validForm - Indicates whether the form is valid.
 */
function handleUpdate(validForm: boolean) {
  validUser.value = validForm
}

</script>
<template>
  <div class="profile-info-wrapper">
    <div class="profile-image">
      <img id="profile-image" :src="profileImage" alt="profileImage" />
    </div>
    <div class="profile-details">
      <UserInfoComponent
        v-if="editableUser"
        v-model:firstName="editableUser.name"
        v-model:lastName="editableUser.surname"
        v-model:email="editableUser.email"
        v-model:countryCode="editableUser.countryCode"
        v-model:phoneNumber="editableUser.phoneNumber"
        v-model:address="editableUser.address"
        v-model:postalCode="editableUser.postalCode"
        v-model:city="editableUser.city"
        :isEditing="isEditing"
        @update="handleUpdate"
      />
      <div v-else class="loading-message">
        Loading user information...
      </div>
      <ProfileButtonsComponent
      :editMode = isEditing
      :logoutMode = isLoggingOut
      :activeSaveButton = validUser
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

.loading-message {
  margin: 25px 0;
  font-style: italic;
  color: #666;
}
</style>
