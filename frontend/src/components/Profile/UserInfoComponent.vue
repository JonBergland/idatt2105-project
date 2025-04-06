<script setup lang="ts">
defineProps<{
  firstName: string | undefined,
  lastName: string | undefined,
  email: string | undefined,
  phoneNumber: string | undefined,
  location: string | undefined,
  isEditing: boolean
}>();

defineEmits([
  'save'
]);
</script>

<template>
  <div v-if="!isEditing" class="user-info-container">
        <h1>{{ firstName + " " + lastName }}</h1>
        <h3>{{ "First name: " + firstName }}</h3>
        <h3>{{ "Last name: " + lastName }}</h3>
        <h3>{{ "Email: " + email }}</h3>
        <h3>{{ "Phone number: " + phoneNumber }}</h3>
        <h3>{{ "Location: " + location }}</h3>
  </div>
  <div v-else-if="isEditing" class="user-info-container">
    <form @submit.prevent="$emit('save')">
      <div class="form-group">
        <label for="firstName">First Name</label>
        <input
          type="text"
          id="firstName"
          class="form-input"
          :value="firstName"
          @input="$emit('update:firstName', ($event.target as HTMLInputElement).value)"
          required
        />
      </div>

      <div class="form-group">
        <label for="lastName">Last Name</label>
        <input
          type="text"
          id="lastName"
          class="form-input"
          :value="lastName"
          @input="$emit('update:lastName', ($event.target as HTMLInputElement).value)"
          required
        />
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          class="form-input"
          :value="email"
          @input="$emit('update:email', ($event.target as HTMLInputElement).value)"
          required
        />
      </div>

      <div class="form-group">
        <label for="phoneNumber">Phone Number</label>
        <input
          type="tel"
          id="phoneNumber"
          class="form-input"
          :value="phoneNumber"
          @input="$emit('update:phoneNumber', ($event.target as HTMLInputElement).value)"
        />
      </div>

      <div class="form-group">
        <label for="location">Location</label>
        <input
          type="text"
          id="location"
          class="form-input"
          :value="location"
          @input="$emit('update:location', ($event.target as HTMLInputElement).value)"
        />
      </div>
    </form>
  </div>
  <div v-else></div>
</template>

<style scoped>
.user-info-container {
  display: flex;
  flex-direction: column;
  align-items: left;
  gap: 20px;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
  width: 100%;
}

.form-group label {
  font-weight: 500;
  margin-bottom: 5px;
  color: var(--color-heading);
}

.form-input {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  width: 100%;
  font-size: 16px;
}

.form-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px rgba(13, 94, 1, 0.1);
}
</style>


