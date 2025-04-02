import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue'

describe('ProductInfoComponent', () => {
  const defaultProps = {
    isAvailable: true,
    price: 1000,
    state: 'New',
    comment: 'Great item!',
    location: 'Trondheim',
    sellerName: 'John Doe'
  }

  it('renders correctly with all props', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.exists()).toBe(true)
  })

  it('displays "Available" when isAvailable is true', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Available')
  })

  it('displays "Not available" when isAvailable is false', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        ...defaultProps,
        isAvailable: false
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Not available')
  })

  it('displays the correct price', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.find('.product-center-header:nth-child(2)').text()).toContain('1000 kr')
  })

  it('displays the state in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.text()).toContain('State: New')
  })

  it('displays the seller comment in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.text()).toContain('Comment from seller: Great item!')
  })

  it('displays the seller location in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.text()).toContain('Location: Trondheim')
  })

  it('displays the seller name in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    expect(wrapper.text()).toContain('Name of seller: John Doe')
  })

  it('has a "Give a bid" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    const button = wrapper.find('.give-bid-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Give a bid')
  })

  it('has a "Pay with vipps" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: defaultProps
    })
    const button = wrapper.find('.vipps-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Pay with vipps')
  })
})
