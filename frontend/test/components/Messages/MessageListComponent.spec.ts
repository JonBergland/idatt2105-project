import { describe, it, expect, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import MessageListComponent from '@/components/Messages/MessagesListComponent.vue';
import MessageItemComponent from '@/components/Messages/MessageItemComponent.vue';
import type { ChatsResponseDTO } from '@/models/message';
import { createPinia, setActivePinia } from 'pinia';
import { useUserStore } from '@/stores/userStore';

describe('MessageListComponent.vue', () => {
  beforeEach(() => {
    setActivePinia(createPinia());

    const userStore = useUserStore();
    userStore.setUser({
      userID: 31, // Using the seller ID from your mock data
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
      longitude: 10.7522
    });
  })

  const mockChats: ChatsResponseDTO = {
    chats: [
      {
        messages: [
          { messageID: 1, senderID: 101, message: 'Hello!', notSeenByUser: false, published: '2023-04-10T14:30:00' },
          { messageID: 2, senderID: 31, message: 'Hi!', notSeenByUser: true, published: '2023-04-10T15:10:00' },
        ],
        buyer: { userID: 101, name: 'Lisa', surname: 'Johnson', email: '', phoneNumber: 0, countryCode: 0, address: '', city: '', postalCode: 0, role: '', latitude: 0, longitude: 0 },
        seller: { userID: 31, name: 'John', surname: 'Doe', email: '', phoneNumber: 0, countryCode: 0, address: '', city: '', postalCode: 0, role: '', latitude: 0, longitude: 0 },
        item: { itemID: 1001, name: 'Vintage Sofa', category: '', sellerID: 31, seller: 'John Doe', description: '', published: '', price: 0, state: '' },
      },
    ],
  };

  it('renders the list of chats', () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItems = wrapper.findAllComponents(MessageItemComponent);
    expect(messageItems.length).toBe(mockChats.chats.length);
  });

  it('displays "Buyer" as the messaging contact role when user is the seller', () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);
    expect(messageItem.props('messagingContactRole')).toBe('Buyer');
  });

  it('displays the name of the messaging contact when the user is the seller', () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);
    expect(messageItem.props('messagingContactName')).toBe(mockChats.chats[0].buyer.name + " " + mockChats.chats[0].buyer.surname);
  });

  it('displays "No messages found" when there are no chats', () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: { chats: [] } },
    });

    expect(wrapper.find('.no-messages').exists()).toBe(true);
    expect(wrapper.find('.no-messages').text()).toBe('No messages found');
  });

  it('emits "chat-selected" when a chat is clicked', async () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);
    await messageItem.trigger('click');

    expect(wrapper.emitted()).toHaveProperty('chat-selected');
    expect(wrapper.emitted('chat-selected')?.[0]).toEqual([mockChats.chats[0]]);
  });

  it('marks the correct chat as active', async () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);
    await messageItem.trigger('click');

    expect(wrapper.findComponent(MessageItemComponent).props('isActive')).toBe(true);
  });

  it('marks the chat as seen', async () => {
    const wrapper = mount(MessageListComponent, {
      props: { chats: mockChats },
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);
    await messageItem.trigger('click');

    for (const message of mockChats.chats[0].messages) {
      expect(message.notSeenByUser).toBe(false);
    }
  });

  it('marks the chat as seen when no messages', async () => {
    const emptyMockChats: ChatsResponseDTO = {
      chats: [
        {
          messages: [],
          buyer: { userID: 101, name: 'Lisa', surname: 'Johnson', email: '', phoneNumber: 0, countryCode: 0, address: '', city: '', postalCode: 0, role: '', latitude: 0, longitude: 0 },
          seller: { userID: 31, name: 'John', surname: 'Doe', email: '', phoneNumber: 0, countryCode: 0, address: '', city: '', postalCode: 0, role: '', latitude: 0, longitude: 0 },
          item: { itemID: 1001, name: 'Vintage Sofa', category: '', sellerID: 31, seller: 'John Doe', description: '', published: '', price: 0, state: '' },
        },
      ],
    };
    const wrapper = mount(MessageListComponent, {
      props: {chats: emptyMockChats}
    });

    const messageItem = wrapper.findComponent(MessageItemComponent);

    expect(messageItem.props('seenByUser')).toBe(true);
  });
});
