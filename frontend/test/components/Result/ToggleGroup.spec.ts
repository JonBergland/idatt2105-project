import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';
import ToggleButton from '@/components/Result/ToggleButton.vue';

describe('ToggleGroup.vue', () => {
  beforeEach(() => {
    vi.useFakeTimers();
  });

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

  it('automatically selects the first toggle when autoSelectFirst is true', async () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
        autoSelectFirst: true
      },
    });
    await flushPromises();

    const firstToggle = wrapper.findAllComponents(ToggleButton)[0];
    expect(firstToggle.props('selected')).toBe(true);

    expect(wrapper.emitted('toggle-selected')).toBeTruthy();
    expect(wrapper.emitted('toggle-selected')?.[0]).toEqual(['Option 1']);
  });

  it('does not auto-select any toggle when autoSelectFirst is false', async () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
        autoSelectFirst: false
      },
    });

    await flushPromises();

    const toggleButtons = wrapper.findAllComponents(ToggleButton);
    toggleButtons.forEach(button => {
      expect(button.props('selected')).toBe(false);
    });

    expect(wrapper.emitted('toggle-selected')).toBeFalsy();
  });

  it('allows deselecting a toggle when allowDeselect is true', async () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
        allowDeselect: true
      },
    });

    const firstToggle = wrapper.findComponent(ToggleButton);
    await firstToggle.trigger('click');

    expect(wrapper.emitted('toggle-selected')?.[0]).toEqual(['Option 1']);

    await firstToggle.trigger('click');

    expect(wrapper.emitted('toggle-selected')?.[1]).toEqual([null]);

    expect(firstToggle.props('selected')).toBe(false);
  });

  it('does not allow deselecting when allowDeselect is false', async () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1', 'Option 2', 'Option 3'],
        direction: 'row',
        allowDeselect: false
      },
    });

    const firstToggle = wrapper.findComponent(ToggleButton);
    await firstToggle.trigger('click');

    expect(wrapper.emitted('toggle-selected')?.[0]).toEqual(['Option 1']);

    await firstToggle.trigger('click');

    expect(wrapper.emitted('toggle-selected')?.length).toBe(1);
    expect(firstToggle.props('selected')).toBe(true);
  });

  it('has the correct default prop values', () => {
    const wrapper = mount(ToggleGroup, {
      props: {
        names: ['Option 1']
      },
    });

    expect(wrapper.props('direction')).toBe('row');
    expect(wrapper.props('autoSelectFirst')).toBe(false);
    expect(wrapper.props('allowDeselect')).toBe(false);
  });
});
