import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import ChatViewComponent from '@/components/Messages/ChatViewComponent.vue';
import { useUserStore } from '@/stores/userStore';
import { createPinia, setActivePinia } from 'pinia';
import type { ChatResponseDTO } from '@/models/message';
import { nextTick } from 'vue';

describe('ChatViewComponent.vue', () => {
  let mockChat: ChatResponseDTO;

  beforeEach(() => {
    setActivePinia(createPinia());

    const userStore = useUserStore();
    userStore.setUser({
      userID: 31, // Current user ID
      name: 'John',
      surname: 'Doe',
      email: 'john.doe@example.com',
      phoneNumber: 12345678,
      countryCode: 47,
      address: '123 Main St',
      city: 'Oslo',
      postalCode: 150,
      role: 'ROLE_USER',
      latitude: 59.9139,
      longitude: 10.7522,
    });

    mockChat = {
      messages: [
        {
          messageID: 1,
          senderID: 101,
          message: 'Hi there! Is the vintage sofa still available?',
          notSeenByUser: false,
          published: '2023-04-10T14:30:00',
        },
        {
          messageID: 2,
          senderID: 31,
          message: "Yes, it's available. Would you like to come see it?",
          notSeenByUser: false,
          published: '2023-04-10T15:10:00',
        },
      ],
      buyer: {
        userID: 101,
        name: 'Lisa',
        surname: 'Johnson',
        email: 'lisa.johnson@example.com',
        phoneNumber: 87654321,
        countryCode: 47,
        address: '456 Second St',
        city: 'Bergen',
        postalCode: 5000,
        role: 'ROLE_USER',
        latitude: 60.3913,
        longitude: 5.3221,
      },
      seller: {
        userID: 31,
        name: 'John',
        surname: 'Doe',
        email: 'john.doe@example.com',
        phoneNumber: 12345678,
        countryCode: 47,
        address: '123 Main St',
        city: 'Oslo',
        postalCode: 150,
        role: 'ROLE_USER',
        latitude: 59.9139,
        longitude: 10.7522,
      },
      item: {
        itemID: 1001,
        name: 'Vintage Sofa',
        category: 'Furniture',
        sellerID: 31,
        seller: 'John Doe',
        description: 'Beautiful vintage sofa from the 1970s, good condition with minor wear.',
        published: '2023-04-01',
        price: 2500,
        state: 'AVAILABLE',
      },
    };
  });

  it('renders the chat messages', () => {
    const wrapper = mount(ChatViewComponent, {
      props: { chat: mockChat },
    });

    const messages = wrapper.findAll('.message');
    expect(messages.length).toBe(mockChat.messages.length);
    expect(messages[0].text()).toContain(mockChat.messages[0].message);
    expect(messages[1].text()).toContain(mockChat.messages[1].message);
  });

  it('applies the correct class for user and other messages', () => {
    const wrapper = mount(ChatViewComponent, {
      props: { chat: mockChat },
    });

    const messages = wrapper.findAll('.message');
    expect(messages[0].classes()).toContain('message-other');
    expect(messages[1].classes()).toContain('message-user');
  });

  it('displays a placeholder when no chat is selected', () => {
    const wrapper = mount(ChatViewComponent, {
      props: { chat: null },
    });

    expect(wrapper.find('.no-chat-selected').exists()).toBe(true);
    expect(wrapper.find('.no-chat-selected').text()).toBe('Select a conversation to start messaging');
  });
});
