import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import ItemGroup from '@/components/Home/ItemGroup.vue';
import ItemCard from '@/components/Home/ItemCard.vue';

describe('ItemGroup.vue', () => {
  const mockItems = [
    { id: 1, name: 'Playstation 5', location: 'Oslo', price: 400, img: 'https://via.placeholder.com/150' },
    { id: 2, name: 'Xbox Series X', location: 'Bærum', price: 500, img: 'https://via.placeholder.com/150' },
    { id: 3, name: 'Nintendo Switch', location: 'Asker', price: 300, img: 'https://via.placeholder.com/150' },
  ];

  it('renders the correct number of ItemCard components', () => {
    const wrapper = mount(ItemGroup, {
      props: { items: mockItems },
    });

    const itemCards = wrapper.findAllComponents(ItemCard);
    expect(itemCards.length).toBe(mockItems.length);
  });

  it('passes the correct props to each ItemCard', () => {
    const wrapper = mount(ItemGroup, {
      props: { items: mockItems },
    });

    const itemCards = wrapper.findAllComponents(ItemCard);
    itemCards.forEach((itemCard, index) => {
      expect(itemCard.props('item')).toEqual(mockItems[index]);
    });
  });

  it('emits "item-clicked" with the correct payload when an ItemCard is clicked', async () => {
    const wrapper = mount(ItemGroup, {
      props: { items: mockItems },
    });

    const firstItemCard = wrapper.findComponent(ItemCard);
    await firstItemCard.vm.$emit('clicked-item', mockItems[0].id);

    expect(wrapper.emitted('item-clicked')).toBeTruthy();
    expect(wrapper.emitted('item-clicked')![0]).toEqual([mockItems[0].id]);
  });

  it('renders nothing if no items are provided', () => {
    const wrapper = mount(ItemGroup, {
      props: { items: [] },
    });

    const itemCards = wrapper.findAllComponents(ItemCard);
    expect(itemCards.length).toBe(0);
  });
});
