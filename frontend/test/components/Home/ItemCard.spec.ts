import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import ItemCard from '@/components/Home/ItemCard.vue';

describe('ItemCard.vue', () => {
    const mockItem = {
    itemID: 1,
    name: 'Playstation 5',
    category: 'Gaming',
    seller: 'Ola Nordmann',
    description: 'Nice playstation',
    published: '2020',
    price: 400,
  };

  it('renders the item details correctly', () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    expect(wrapper.text()).toContain(mockItem.name);
    expect(wrapper.text()).toContain(`${mockItem.price} kr`);

  });

  it('emits "clicked-item" with the correct payload when clicked', async () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    await wrapper.find('.item-card').trigger('click');

    expect(wrapper.emitted('clicked-item')).toBeTruthy();
    expect(wrapper.emitted('clicked-item')![0]).toEqual([mockItem.itemID]);
  });

  it('has the correct default styles', () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    const card = wrapper.find('.item-card');

    expect(card.classes()).toContain('item-card');
  });
});
