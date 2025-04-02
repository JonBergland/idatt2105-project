import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import ItemCard from '@/components/Home/ItemCard.vue';

describe('ItemCard.vue', () => {
  const mockItem = {
    id: 1,
    name: 'Playstation 5',
    location: 'Oslo',
    price: 400,
    img: 'https://via.placeholder.com/150',
    alt: 'Playstation 5 Image',
  };

  it('renders the item details correctly', () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    expect(wrapper.text()).toContain(mockItem.name);
    expect(wrapper.text()).toContain(mockItem.location);
    expect(wrapper.text()).toContain(`${mockItem.price} kr`);

    const img = wrapper.find('img');
    expect(img.attributes('src')).toBe(mockItem.img);
    expect(img.attributes('alt')).toBe(mockItem.alt);
  });

  it('emits "clicked-item" with the correct payload when clicked', async () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    await wrapper.find('.item-card').trigger('click');

    expect(wrapper.emitted('clicked-item')).toBeTruthy();
    expect(wrapper.emitted('clicked-item')![0]).toEqual([mockItem.id]);
  });

  it('has the correct default styles', () => {
    const wrapper = mount(ItemCard, {
      props: { item: mockItem },
    });

    const card = wrapper.find('.item-card');

    expect(card.classes()).toContain('item-card');
  });
});
