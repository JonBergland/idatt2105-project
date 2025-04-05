import { describe, it, expect} from 'vitest'
import { mount } from '@vue/test-utils';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';
import ToggleButton from '@/components/Result/ToggleButton.vue';

describe('ToggleGroup.vue', () => {
  it('renders the correct number of ToggleButton components', () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
      },
    });

    const toggleButtons = wrapper.findAllComponents(ToggleButton);
    expect(toggleButtons.length).toBe(3);
  });

  it('emits the "toggle-selected" event when a toggle is clicked', async () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
      },
    });

    const firstToggle = wrapper.findComponent(ToggleButton);
    await firstToggle.trigger('click');

    expect(wrapper.emitted('toggle-selected')).toBeTruthy();
    expect(wrapper.emitted('toggle-selected')?.[0]).toEqual(['Option 1']);
  });

  it('applies the correct direction class', () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'column',
      },
    });

    expect(wrapper.classes()).toContain('column');
  });

  it('renders the label if provided', () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        label: 'Test Label',
        names: ['Option 1', 'Option 2'],
        direction: 'row',
      },
    });

    expect(wrapper.text()).toContain('Test Label');
  });

  it('does not render the label if not provided', () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2'],
        direction: 'row',
      },
    });

    expect(wrapper.find('p').exists()).toBe(false);
  });
});
