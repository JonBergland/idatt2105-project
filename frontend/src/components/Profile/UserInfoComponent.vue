<script setup lang="ts">
defineProps<{
  firstName: string | undefined,
  lastName: string | undefined,
  email: string | undefined,
  phoneNumber: string | undefined,
  location: string | undefined
}>()
</script>

<template>
  <div class="user-info-container">
        <h1>{{ firstName + " " + lastName }}</h1>
        <h3>{{ "First name: " + firstName }}</h3>
        <h3>{{ "Last name: " + lastName }}</h3>
        <h3>{{ "Email: " + email }}</h3>
        <h3>{{ "Phone number: " + phoneNumber }}</h3>
        <h3>{{ "Location: " + location }}</h3>
      </div>
</template>

<style scoped>
.user-info-container {
  display: flex;
  flex-direction: column;
  align-items: left;
  gap: 20px;
  width: 100%;
}
</style>


