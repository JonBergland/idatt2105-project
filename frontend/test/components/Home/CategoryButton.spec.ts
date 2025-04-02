import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import CategoryButton from '@/components/Home/CategoryButton.vue';

describe('CategoryButton.vue', () => {
  it('renders the button with the correct name', () => {
    const name = 'Test Category';
    const wrapper = mount(CategoryButton, {
      props: { name },
    });
    expect(wrapper.text()).toBe(name);
  });

  it('emits "clicked-category" with the correct payload when clicked', async () => {
    const name = 'Test Category';
    const wrapper = mount(CategoryButton, {
      props: { name },
    });

    await wrapper.find('button').trigger('click');

    expect(wrapper.emitted('clicked-category')).toBeTruthy();
    expect(wrapper.emitted('clicked-category')![0]).toEqual([name]);
  });
});
