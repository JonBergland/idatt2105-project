<script setup lang="ts">
const props = defineProps<{
  messages: string[] // Create DTO
}>()
</script>
<template>
  <div class="messages-list-wrapper">
    <!-- Header -->
     <h1>Messages</h1>
     <div class="messages-list">
      <!-- List of messages -->
     </div>

  </div>

</template>
<style>
</style>
