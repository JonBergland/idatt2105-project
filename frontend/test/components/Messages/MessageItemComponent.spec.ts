import { describe, it, expect, afterEach } from 'vitest'
import { mount } from '@vue/test-utils';
import MessageItemComponent from '@/components/Messages/MessageItemComponent.vue';

describe('MessageItemComponent.vue', () => {
  const defaultProps = {
    itemName: 'Test Item',
    messagingContactRole: 'Buyer',
    messagingContactName: 'John Doe',
    productImage: '',
    isActive: false,
    seenByUser: true,
  };

  it('renders the component with default props', () => {
    const wrapper = mount(MessageItemComponent, {
      props: defaultProps,
    });

    expect(wrapper.find('.item-name').text()).toBe(defaultProps.itemName);
    expect(wrapper.find('.messaging-contact-name').text()).toBe(
      `${defaultProps.messagingContactRole}: ${defaultProps.messagingContactName}`
    );
    expect(wrapper.find('img').attributes('src')).toBeTruthy();
    expect(wrapper.find('.new-message-indicator').exists()).toBe(false);
  });

  it('shows the new message indicator when seenByUser is false', () => {
    const wrapper = mount(MessageItemComponent, {
      props: { ...defaultProps, seenByUser: false },
    });

    expect(wrapper.find('.new-message-indicator').exists()).toBe(true);
  });

  it('applies the active class when isActive is true', () => {
    const wrapper = mount(MessageItemComponent, {
      props: { ...defaultProps, isActive: true },
    });

    expect(wrapper.classes()).toContain('active');
  });

  it('displays the placeholder image when productImage is not provided', () => {
    const wrapper = mount(MessageItemComponent, {
      props: { ...defaultProps, productImage: '' },
    });

    expect(wrapper.find('img').attributes('src')).toContain('placeholder-image.png');
  });

  it('displays the provided product image when productImage is set', () => {
    const testImage = 'test-image-url.png';
    const wrapper = mount(MessageItemComponent, {
      props: { ...defaultProps, productImage: testImage },
    });

    expect(wrapper.find('img').attributes('src')).toBe(testImage);
  });
});
