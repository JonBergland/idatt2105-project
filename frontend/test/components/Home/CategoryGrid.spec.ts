import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import CategoryGrid from '@/components/Home/CategoryGrid.vue';
import CategoryButton from '@/components/Home/CategoryButton.vue';

describe('CategoryGrid.vue', () => {
  it('renders the correct number of CategoryButton components', () => {
    const categories = ['Category 1', 'Category 2', 'Category 3'];
    const wrapper = mount(CategoryGrid, {
      props: { categories },
    });

    const buttons = wrapper.findAllComponents(CategoryButton);
    expect(buttons.length).toBe(categories.length);
  });

  it('passes the correct props to each CategoryButton', () => {
    const categories = ['Category 1', 'Category 2', 'Category 3'];
    const wrapper = mount(CategoryGrid, {
      props: { categories },
    });

    const buttons = wrapper.findAllComponents(CategoryButton);
    buttons.forEach((button, index) => {
      expect(button.props('name')).toBe(categories[index]);
    });
  });

  it('emits "category-clicked" with the correct payload when a button is clicked', async () => {
    const categories = ['Category 1', 'Category 2', 'Category 3'];
    const wrapper = mount(CategoryGrid, {
      props: { categories },
    });

    const firstButton = wrapper.findComponent(CategoryButton);
    await firstButton.vm.$emit('clicked-category', categories[0]);

    expect(wrapper.emitted('category-clicked')).toBeTruthy();
    expect(wrapper.emitted('category-clicked')![0]).toEqual([categories[0]]);
  });

  it('renders nothing if no categories are provided', () => {
    const wrapper = mount(CategoryGrid, {
      props: { categories: [] },
    });

    const buttons = wrapper.findAllComponents(CategoryButton);
    expect(buttons.length).toBe(0);
  });
});
