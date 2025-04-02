import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue'

// Mock SVG imports
vi.mock('@/assets/icons/heart.svg', () => ({
  default: 'heart.svg',
}))
vi.mock('@/assets/icons/heart-selected.svg', () => ({
  default: 'heart-selected.svg',
}))
vi.mock('@/assets/icons/image-arrow-left.svg', () => ({
  default: 'image-arrow-left.svg',
}))
vi.mock('@/assets/icons/image-arrow-right.svg', () => ({
  default: 'image-arrow-right.svg',
}))

describe('ProductImageComponent', () => {
  const mockImages = [
    'https://example.com/image1.jpg',
    'https://example.com/image2.jpg',
    'https://example.com/image3.jpg',
  ]

  it('renders the first image by default', () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const productImage = wrapper.find('.product-image')
    expect(productImage.attributes('style')).toContain(`url(${mockImages[0]})`)
  })

  it('navigates to the next image when the right button is clicked', async () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const nextButton = wrapper.find('.nav-button.right')
    await nextButton.trigger('click')
    const productImage = wrapper.find('.product-image')
    expect(productImage.attributes('style')).toContain(`url(${mockImages[1]})`)
  })

  it('navigates to the previous image when the left button is clicked', async () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const nextButton = wrapper.find('.nav-button.right')
    const prevButton = wrapper.find('.nav-button.left')
    await nextButton.trigger('click')
    await prevButton.trigger('click')
    const productImage = wrapper.find('.product-image')
    expect(productImage.attributes('style')).toContain(`url(${mockImages[0]})`)
  })

  it('does not navigate to the previous image if already on the first image', async () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const prevButton = wrapper.find('.nav-button.left')
    await prevButton.trigger('click')
    const productImage = wrapper.find('.product-image')
    expect(productImage.attributes('style')).toContain(`url(${mockImages[0]})`)
  })

  it('does not navigate to the next image if already on the last image', async () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const nextButton = wrapper.find('.nav-button.right')
    await nextButton.trigger('click')
    await nextButton.trigger('click')
    await nextButton.trigger('click')
    const productImage = wrapper.find('.product-image')
    expect(productImage.attributes('style')).toContain(`url(${mockImages[2]})`)
  })

  it('toggles the favorite state when the favorite button is clicked', async () => {
    const wrapper = mount(ProductImageComponent, {
      props: { images: mockImages },
    })
    const favoriteButton = wrapper.find('.favorite-button')
    const favoriteIcon = () => wrapper.find('.favorite-button img')

    expect(favoriteIcon().attributes('src')).toContain('heart.svg')
    await favoriteButton.trigger('click')
    expect(favoriteIcon().attributes('src')).toContain('heart-selected.svg')
    await favoriteButton.trigger('click')
    expect(favoriteIcon().attributes('src')).toContain('heart.svg')
  })
})
