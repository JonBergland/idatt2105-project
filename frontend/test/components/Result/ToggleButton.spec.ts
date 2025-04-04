import { describe, it, expect} from 'vitest'
import { mount } from '@vue/test-utils';
import ToggleButton from '@/components/Result/ToggleButton.vue';

describe('ToggleButton.vue', () => {
  it('renders the button with the correct name', () => {
    const wrapper = mount(ToggleButton, {
      props: {
        name: 'Test Button',
        selected: false,
      },
    });

    expect(wrapper.text()).toBe('Test Button');
  });

  it('applies the "selected" class when the selected prop is true', () => {
    const wrapper = mount(ToggleButton, {
      props: {
        name: 'Test Button',
        selected: true,
      },
    });
    expect(wrapper.classes()).toContain('selected');
  });

  it('does not apply the "selected" class when the selected prop is false', () => {
    const wrapper = mount(ToggleButton, {
      props: {
        name: 'Test Button',
        selected: false,
      },
    });

    expect(wrapper.classes()).not.toContain('selected');
  });

  it('emits the "clicked-toggle" event with the correct payload when clicked', async () => {
    const wrapper = mount(ToggleButton, {
      props: {
        name: 'Test Button',
        selected: false,
      },
    });

    await wrapper.trigger('click');

    expect(wrapper.emitted('clicked-toggle')).toBeTruthy();
    expect(wrapper.emitted('clicked-toggle')?.[0]).toEqual(['Test Button']);
  });
});
