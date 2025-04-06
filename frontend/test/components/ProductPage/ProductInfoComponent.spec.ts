import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue'

describe('ProductInfoComponent', () => {
  // Mock ItemResponseDTO
  const defaultItem = {
    itemID: 1,
    name: 'Test Item',
    price: 1000,
    category: 'Electronics',
    seller: 'John Doe',
    description: 'Great item!',
    published: '2023-04-01',
    state: 'available',
    images: []
  }

  it('renders correctly with all props', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.exists()).toBe(true)
  })

  it('displays "Available" when state is available', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Available')
  })

  it('displays "Not Available: Archived" when state is archived', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: {
          ...defaultItem,
          state: 'archived'
        }
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Not Available: Archived')
  })

  it('displays "Reserved" when state is reserved', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: {
          ...defaultItem,
          state: 'reserved'
        }
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Reserved')
  })

  it('displays "Not Available: Sold" when state is sold', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: {
          ...defaultItem,
          state: 'sold'
        }
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Not Available: Sold')
  })

  it('displays the correct price', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.find('.product-center-header:nth-child(2)').text()).toContain('1000 kr')
  })

  it('displays the published date in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.text()).toContain('Published: 2023-04-01')
  })

  it('displays the category in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.text()).toContain('Category: Electronics')
  })

  it('displays the description in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.text()).toContain('Description: Great item!')
  })

  it('displays the seller name in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    expect(wrapper.text()).toContain('Name of seller: John Doe')
  })

  it('has a "Give a bid" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    const button = wrapper.find('.give-bid-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Give a bid')
  })

  it('has a "Pay with vipps" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      }
    })
    const button = wrapper.find('.vipps-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Pay with vipps')
  })
})
