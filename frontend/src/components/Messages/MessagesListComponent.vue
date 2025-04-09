<script setup lang="ts">
import type { ChatsResponseDTO, ChatResponseDTO } from '@/models/message';
import MessageItemComponent from '@/components/Messages/MessageItemComponent.vue';
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';

const props = defineProps<{
  chats: ChatsResponseDTO
}>();

const userStore = useUserStore()

const emit = defineEmits(['chat-selected']);

// Track the active chat ID
const activeChatId = ref<number | null>(null);

/**
 * Handles when a message item is clicked
 * Sets the active chat and emits the selected chat to parent
 *
 * @param chat The chat that was selected
 */
function handleMessageItemClick(chat: ChatResponseDTO) {
  activeChatId.value = chat.item.itemID;

  chat.messages.forEach(messsage => {
    messsage.notSeenByUser = false
  });

  emit('chat-selected', chat);
}

/**
 * Determines if the current user is the seller based on the provided seller ID.
 *
 * @param {number} sellerID - The ID of the seller to check against the current user.
 * @returns {boolean} - Returns true if the current user is the seller, otherwise false.
 */
function isUserSeller(sellerID: number) {
  return sellerID === userStore.user?.userID
}

/**
 * Determines the role of the messaging contact based on the chat.
 *
 * @param chat The chat to analyze
 * @returns "Buyer" or "Seller" based on who is the contact
 */
function getMessagingContactRole(chat: ChatResponseDTO): string {
  return isUserSeller(chat.item.sellerID) ?"Buyer" : "Seller";
}

/**
 * Gets the name of the messaging contact
 *
 * @param chat The chat to analyze
 * @returns The name of the contact person
 */
function getMessagingContactName(chat: ChatResponseDTO): string {
  return isUserSeller(chat.item.sellerID) ? `${chat.buyer.name} ${chat.buyer.surname}` : `${chat.seller.name} ${chat.seller.surname}`;
}

/**
 * Checks if the latest message in a chat has been seen by the user
 *
 * @param chat The chat to check
 * @returns true if the latest message has been seen, false otherwise
 */
function isLatestMessageSeen(chat: ChatResponseDTO): boolean {
  if (!chat.messages || chat.messages.length === 0) {
    return true;
  }

  const latestMessage = chat.messages[chat.messages.length - 1];
  return !latestMessage.notSeenByUser;
}

defineExpose({
  getMessagingContactName,
  getMessagingContactRole
});
</script>

<template>
  <div class="messages-list-wrapper">
    <!-- Header -->
    <h1>Messages</h1>
    <div class="messages-list">
      <!-- List of messages -->
      <div v-if="props.chats && props.chats.chats && props.chats.chats.length > 0">
        <!-- TODO: Add image url when that is implemented -->
        <MessageItemComponent
          v-for="chat in props.chats.chats"
          :key="chat.item.itemID"
          :itemName="chat.item.name"
          :messagingContactRole="getMessagingContactRole(chat)"
          :messagingContactName="getMessagingContactName(chat)"
          :productImage="''"
          :isActive="activeChatId === chat.item.itemID"
          :seenByUser="isLatestMessageSeen(chat)"
          @click="handleMessageItemClick(chat)"
        />
      </div>
      <div v-else class="no-messages">
        <p>No messages found</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.messages-list-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  border-right: 1px solid #e0e0e0;
  overflow: hidden;
}

.messages-list-wrapper h1 {
  padding: 20px;
  margin: 0;
  font-size: 24px;
  border-bottom: 1px solid #e0e0e0;
}

.messages-list {
  overflow-y: auto;
  flex: 1;
  padding: 10px;
}

.messages-list > div {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.no-messages {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #666;
  font-style: italic;
}
</style>
