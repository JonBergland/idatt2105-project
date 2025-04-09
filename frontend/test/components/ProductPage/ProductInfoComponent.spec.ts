import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue'
import { nextTick } from 'vue'

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
        },
        isAuth: true
      }
    })

    await wrapper.vm.$nextTick()
    expect(wrapper.find('.product-center-header').text()).toContain('Not Available: Sold')
  })

  it('displays the correct price', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    expect(wrapper.find('.product-center-header:nth-child(2)').text()).toContain('1000 kr')
  })

  it('displays the published date in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    expect(wrapper.text()).toContain('Published: 2023-04-01')
  })

  it('displays the category in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    expect(wrapper.text()).toContain('Category: Electronics')
  })

  it('displays the description in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    expect(wrapper.text()).toContain('Description: Great item!')
  })

  it('displays the seller name in the product information', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    expect(wrapper.text()).toContain('Name of seller: John Doe')
  })

  it('has a "Give a bid" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    const button = wrapper.find('.give-bid-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Give a bid')
  })

  it('has a "Pay with vipps" button', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem
      },
      isAuth: true
    })
    const button = wrapper.find('.vipps-button')
    expect(button.exists()).toBe(true)
    expect(button.text()).toBe('Pay with vipps')
  })

  it('emits bid event with correct value when valid bid is submitted', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('#bid').setValue('1500')
    await wrapper.find('.give-bid-button').trigger('click')

    expect(wrapper.emitted()).toHaveProperty('bid')
    expect(wrapper.emitted('bid')[0]).toEqual([1500])

    expect((wrapper.find('#bid').element as HTMLInputElement).value).toBe('')
  })

  it('shows error message when empty bid is submitted', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(true)
    expect(wrapper.find('.error-message').text()).toBe('Please enter a valid bid amount')

    expect(wrapper.emitted('bid')).toBeFalsy()
  })

  it('shows error message when invalid bid (not a number) is submitted', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('#bid').setValue('abc')
    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(true)
    expect(wrapper.find('.error-message').text()).toBe('Please enter a valid bid amount')

    expect(wrapper.emitted('bid')).toBeFalsy()
  })

  it('shows error message when negative bid is submitted', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('#bid').setValue('-100')
    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(true)
    expect(wrapper.find('.error-message').text()).toBe('Please enter a valid bid amount')

    expect(wrapper.emitted('bid')).toBeFalsy()
  })

  it('shows error message when zero bid is submitted', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('#bid').setValue('0')
    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(true)
    expect(wrapper.find('.error-message').text()).toBe('Please enter a valid bid amount')

    expect(wrapper.emitted('bid')).toBeFalsy()
  })

  it('clears error message when valid bid is submitted after an invalid one', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    await wrapper.find('#bid').setValue('')
    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(true)

    await wrapper.find('#bid').setValue('2000')
    await wrapper.find('.give-bid-button').trigger('click')

    await nextTick()
    expect(wrapper.find('.error-message').exists()).toBe(false)

    expect(wrapper.emitted('bid')).toBeTruthy()
    expect(wrapper.emitted('bid')![0]).toEqual([2000])
  })

  it('emits login event when not authenticated and bid button is clicked', async () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: false
      }
    })

    await wrapper.find('.give-bid-button').trigger('click')

    expect(wrapper.emitted()).toHaveProperty('login')
    expect(wrapper.emitted('bid')).toBeFalsy()
  })

  it('does not show bid input field when not authenticated', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: false
      }
    })

    expect(wrapper.find('#bid').exists()).toBe(false)
    expect(wrapper.find('.bid-input-container').exists()).toBe(false)
  })

  it('shows bid input field when authenticated', () => {
    const wrapper = mount(ProductInfoComponent, {
      props: {
        item: defaultItem,
        isAuth: true
      }
    })

    expect(wrapper.find('#bid').exists()).toBe(true)
    expect(wrapper.find('.bid-input-container').exists()).toBe(true)
  })
})
