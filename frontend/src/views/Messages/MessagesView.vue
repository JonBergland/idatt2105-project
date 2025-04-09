<script setup lang="ts">
import ChatViewComponent from '@/components/Messages/ChatViewComponent.vue';
import MessagesListComponent from '@/components/Messages/MessagesListComponent.vue';
import type { ChatsResponseDTO, ChatResponseDTO } from '@/models/message';
import { useAuthStore } from '@/stores/authStore';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import "@/assets/color.css"
import "@/assets/base.css"
import "@/assets/main.css"
import { useUserStore } from '@/stores/userStore';

const authStore = useAuthStore()
const userStore = useUserStore()
const router = useRouter()
const chats = ref<ChatsResponseDTO>({ chats: [] });
const selectedChat = ref<ChatResponseDTO | null>(null);

const mockChats: ChatsResponseDTO = {
  chats: [
    {
      messages: [
        {
          messageID: 1,
          senderID: 101,
          message: "Hi there! Is the vintage sofa still available?",
          notSeenByUser: false,
          published: "2023-04-10T14:30:00"
        },
        {
          messageID: 2,
          senderID: 31,
          message: "Yes, it's available. Would you like to come see it?",
          notSeenByUser: false,
          published: "2023-04-10T15:10:00"
        },
        {
          messageID: 3,
          senderID: 101,
          message: "Great! What time works for you?",
          notSeenByUser: true,
          published: "2023-04-10T15:30:00"
        }
      ],
      buyer: {
        userID: 101,
        name: "Lisa",
        surname: "Johnson",
        email: "lisa.johnson@example.com",
        phoneNumber: 87654321,
        countryCode: 47,
        address: "456 Second St",
        city: "Bergen",
        postalCode: 5000,
        role: "ROLE_USER",
        latitude: 60.3913,
        longitude: 5.3221
      },
      seller: {
        userID: 31,
        name: "John",
        surname: "Doe",
        email: "john.doe@example.com",
        phoneNumber: 12345678,
        countryCode: 47,
        address: "123 Main St",
        city: "Oslo",
        postalCode: 150,
        role: "ROLE_USER",
        latitude: 59.9139,
        longitude: 10.7522
      },
      item: {
        itemID: 1001,
        name: "Vintage Sofa",
        category: "Furniture",
        sellerID: 31,
        seller: "John Doe",
        description: "Beautiful vintage sofa from the 1970s, good condition with minor wear.",
        published: "2023-04-01",
        price: 2500,
        state: "AVAILABLE"
      }
    },

    {
      messages: [
        {
          messageID: 11,
          senderID: 102,
          message: "Hi, I'm interested in your mountain bike. Does it have front suspension?",
          notSeenByUser: false,
          published: "2023-04-11T09:20:00"
        },
        {
          messageID: 12,
          senderID: 31,
          message: "Yes, it has front suspension and disc brakes.",
          notSeenByUser: false,
          published: "2023-04-11T09:45:00"
        },
        {
          messageID: 13,
          senderID: 102,
          message: "Great, would you consider a lower price?",
          notSeenByUser: false,
          published: "2023-04-11T10:10:00"
        },
        {
          messageID: 14,
          senderID: 31,
          message: "I could go down to 3800 kr.",
          notSeenByUser: true,
          published: "2023-04-11T10:25:00"
        }
      ],
      buyer: {
        userID: 102,
        name: "Erik",
        surname: "Andersen",
        email: "erik.andersen@example.com",
        phoneNumber: 98765432,
        countryCode: 47,
        address: "789 Third St",
        city: "Trondheim",
        postalCode: 7000,
        role: "ROLE_USER",
        latitude: 63.4305,
        longitude: 10.3951
      },
      seller: {
        userID: 31,
        name: "John",
        surname: "Doe",
        email: "john.doe@example.com",
        phoneNumber: 12345678,
        countryCode: 47,
        address: "123 Main St",
        city: "Oslo",
        postalCode: 150,
        role: "ROLE_USER",
        latitude: 59.9139,
        longitude: 10.7522
      },
      item: {
        itemID: 1002,
        name: "Mountain Bike",
        category: "Sports",
        sellerID: 31,
        seller: "John Doe",
        description: "High-quality mountain bike, used for 2 seasons. 27 gears, disc brakes.",
        published: "2023-04-05",
        price: 4000,
        state: "AVAILABLE"
      }
    },

    {
      messages: [
        {
          messageID: 21,
          senderID: 31,
          message: "Hello, I saw your iPhone listing. What's the battery health?",
          notSeenByUser: false,
          published: "2023-04-12T18:15:00"
        },
        {
          messageID: 22,
          senderID: 103,
          message: "Hi! The battery health is at 89%.",
          notSeenByUser: true,
          published: "2023-04-12T18:30:00"
        }
      ],
      seller: {
        userID: 103,
        name: "Maria",
        surname: "Hansen",
        email: "maria.hansen@example.com",
        phoneNumber: 45678901,
        countryCode: 47,
        address: "101 Fourth St",
        city: "Stavanger",
        postalCode: 4000,
        role: "ROLE_USER",
        latitude: 58.9700,
        longitude: 5.7331
      },
      buyer: {
        userID: 31,
        name: "John",
        surname: "Doe",
        email: "john.doe@example.com",
        phoneNumber: 12345678,
        countryCode: 47,
        address: "123 Main St",
        city: "Oslo",
        postalCode: 150,
        role: "ROLE_USER",
        latitude: 59.9139,
        longitude: 10.7522
      },
      item: {
        itemID: 1003,
        name: "iPhone 13",
        category: "Electronics",
        sellerID: 103,
        seller: "Maria Hansen",
        description: "iPhone 13, 128GB, Blue, bought last year. Comes with original charger and box.",
        published: "2023-04-08",
        price: 6500,
        state: "AVAILABLE"
      }
    }
  ]
};

onMounted(async () => {
  if (!authStore.isAuth) {
    try {
      const isAuthenticated: boolean = await authStore.checkIfAuth();
      if (!isAuthenticated) {
        router.push({ name: 'login' });
        return;
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      router.push({ name: 'login' });
      return;
    }
  }

  // TODO: Retrieve chats from backend
  chats.value = mockChats;

  checkForUnseenMessages();

});

/**
 * Handles when a chat is selected from the message list.
 *
 * @param chat The selected chat
 */
 function handleChatSelected(chat: ChatResponseDTO) {
  selectedChat.value = chat;
  console.log("Selected chat:", chat);

  checkForUnseenMessages();
}

/**
 * Checks for any unseen messages in the system.
 * This function is responsible for determining if there are messages
 * that have not yet been viewed by the user.
 */
function checkForUnseenMessages() {
  let hasUnseenMessages = false;

  if (chats.value && chats.value.chats) {
    for (const chat of chats.value.chats) {
      if (!chat.messages || chat.messages.length === 0) continue;

      const unseenMessages = chat.messages.some(message => message.notSeenByUser);

      if (unseenMessages) {
        hasUnseenMessages = true;
        break;
      }
    }
  }

  userStore.messagesNotSeen = hasUnseenMessages;
}

</script>
<template>
  <!-- Div if screen is wide enough -->
  <div class="messages-page-wrapper">
    <div class="messages-list">
      <!-- Messages list component -->
      <MessagesListComponent
      :chats="chats"
      @chat-selected="handleChatSelected"
      />
    </div>
    <div class="chat-view">
      <!-- Chat view -->
       <ChatViewComponent
       :chat = "selectedChat"
       />
    </div>
  </div>
</template>
<style scoped>
.messages-page-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}

.messages-list {
  width: 30%;
  border-right: 1px solid var(--color-border);
  overflow-y: auto;
}

.chat-view {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
</style>
