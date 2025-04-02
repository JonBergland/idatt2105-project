import { describe, it, expect, beforeEach, vi } from 'vitest'
import { shallowMount } from '@vue/test-utils'
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import backArrowWhite from '@/assets/icons/back-arrow-white.svg'
import backArrowBlack from '@/assets/icons/back-arrow-black.svg'

describe('ProductNameComponent', () => {
  beforeEach(() => {
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({
        matches: false,
        media: query,
        onchange: null,
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
      }))
    })
  })

  it('renders correctly', () => {
    const wrapper = shallowMount(ProductNameComponent, {
      props: { productName: 'Test Product' }
    })
    expect(wrapper.exists()).toBe(true)
  })

  it('displays the correct product name', () => {
    const productName = 'Test Product'
    const wrapper = shallowMount(ProductNameComponent, {
      props: { productName }
    })
    expect(wrapper.text()).toContain(productName)
  })

  it('emits backClick event when the back arrow is clicked', async () => {
    const wrapper = shallowMount(ProductNameComponent, {
      props: { productName: 'Test Product' }
    })

    const backArrow = wrapper.find('img.icon')
    await backArrow.trigger('click')

    expect(wrapper.emitted()).toHaveProperty('backClick')
    expect(wrapper.emitted('backClick')?.length).toBe(1)
  })

  it('sets the correct back arrow based on dark mode preference', async () => {
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({
        matches: query.includes('dark'),
        media: query,
        addEventListener: vi.fn(),
        removeEventListener: vi.fn()
      }))
    })

    const wrapper = shallowMount(ProductNameComponent, {
      props: { productName: 'Test Product' }
    })

    expect(wrapper.vm.backArrow).toBe(backArrowWhite)
  })

  it('sets the correct back arrow based on light mode preference', async () => {
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({
        matches: query.includes('light'),
        media: query,
        addEventListener: vi.fn(),
        removeEventListener: vi.fn()
      }))
    })

    const wrapper = shallowMount(ProductNameComponent, {
      props: { productName: 'Test Product' }
    })

    expect(wrapper.vm.backArrow).toBe(backArrowBlack)
  })
})
