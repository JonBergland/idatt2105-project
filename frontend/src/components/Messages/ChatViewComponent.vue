<script setup lang="ts">
import type { ChatResponseDTO } from '@/models/message';
import { useUserStore } from '@/stores/userStore';
import { onMounted, ref, watch } from 'vue';

const userStore = useUserStore();

const props = defineProps<{
  chat: ChatResponseDTO
}>();

const messagesContainer = ref<HTMLElement | null>(null);

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};

watch(
  () => props.chat?.messages,
  () => {
    scrollToBottom();
  }
);

onMounted(() => {
  scrollToBottom();
});
</script>

<template>
  <div class="chat-view-wrapper">
    <div v-if="chat" class="chat-container">
      <div ref="messagesContainer" class="messages">
        <div
          v-for="message in chat.messages"
          :key="message.messageID"
          :class="['message', { 'message-user': message.senderID === userStore.user?.userID, 'message-other': message.senderID !== userStore.user?.userID }]"
        >
          <div class="message-content">
            {{ message.message }}
          </div>
          <span class="message-time">{{ new Date(message.published).toLocaleTimeString() }}</span>
        </div>
      </div>
    </div>
    <div v-else class="no-chat-selected">
      <p>Select a conversation to start messaging</p>
    </div>
  </div>
</template>

<style scoped>
.chat-view-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #f5f5f5;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message {
  max-width: 70%;
  padding: 10px;
  border-radius: 10px;
  font-size: 14px;
  position: relative;
}

.message-user {
  align-self: flex-end;
  background-color: black;
  color: white;
  text-align: right;
}

.message-other {
  align-self: flex-start;
  background-color: #e0e0e0;
  color: black;
  text-align: left;
}

.message-content {
  word-wrap: break-word;
}

.message-time {
  font-size: 10px;
  margin-top: 5px;
  display: block;
}

.no-chat-selected {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #888;
  font-size: 16px;
}
</style>

