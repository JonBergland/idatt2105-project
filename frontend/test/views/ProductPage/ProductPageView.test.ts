import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import { setActivePinia, createPinia } from 'pinia';
import ProductPageView from '@/views/ProductPage/ProductPageView.vue';

vi.mock('@/components/ProductPage/ProductImageComponent.vue', () => ({
  default: {
    name: 'ProductImageComponent',
    template: '<div class="mock-image-component">Mock Image</div>'
  }
}));

vi.mock('@/components/ProductPage/ProductNameComponent.vue', () => ({
  default: {
    name: 'ProductNameComponent',
    template: '<div class="mock-name-component">Mock Name <button @click="$emit(\'back-click\')">Back</button></div>'
  }
}));

vi.mock('@/components/ProductPage/ProductInfoComponent.vue', () => ({
  default: {
    name: 'ProductInfoComponent',
    template: '<div class="mock-info-component">Mock Info</div>'
  }
}));

vi.mock('@/components/NewListing/ItemForm.vue', () => ({
  default: {
    name: 'ItemForm',
    template: '<div class="mock-item-form">Mock Form <button @click="$emit(\'submit\', testItem)">Submit</button></div>',
    setup() {
      return {
        testItem: {
          name: 'Updated Name',
          description: 'Updated Description',
          price: 150,
          category: 'Electronics'
        }
      };
    }
  }
}));

vi.mock('@/stores/authStore', () => ({
  useAuthStore: () => ({
    isAuth: true,
    checkIfAuth: vi.fn().mockResolvedValue(true)
  })
}));

vi.mock('@/stores/userStore', () => ({
  useUserStore: () => ({
    user: { email: 'test@example.com' },
    item: {
      itemID: 123,
      name: 'Test Product',
      description: 'Test Description',
      price: 100,
      category: 'Electronics',
      seller: 'test@example.com',
      state: 'available'
    },
    fetchUserItemDetails: vi.fn().mockResolvedValue(undefined),
    getUserInfo: vi.fn().mockResolvedValue(undefined),
    updateItemDetails: vi.fn().mockResolvedValue(true)
  })
}));

vi.mock('@/stores/itemStore', () => ({
  useItemStore: () => ({
    item: {
      itemID: 123,
      name: 'Test Product',
      description: 'Test Description',
      price: 100,
      category: 'Electronics'
    },
    fetchItemDetails: vi.fn().mockResolvedValue(undefined),
    fetchCategories: vi.fn()
  })
}));

const mockRouter = {
  back: vi.fn(),
  push: vi.fn()
};

vi.mock('vue-router', () => ({
  useRoute: () => ({
    query: { id: '123' }
  }),
  useRouter: () => mockRouter
}));

describe('ProductPageView', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setActivePinia(createPinia());
  });

  it('renders the product page', () => {
    const wrapper = mount(ProductPageView);
    expect(wrapper.exists()).toBe(true);
  });

  it('handles back button navigation', async () => {
    const wrapper = mount(ProductPageView);

    const backButton = wrapper.find('.mock-name-component button');
    await backButton.trigger('click');

    expect(mockRouter.back).toHaveBeenCalled();
  });

  it('renders my listing container when isMyItem is true', async () => {
    const wrapper = mount(ProductPageView);

    wrapper.vm.isMyItem = true;
    await wrapper.vm.$nextTick();

    const myListingContainer = wrapper.find('.my-listing-container');
    expect(myListingContainer.exists()).toBe(true);
    expect(myListingContainer.find('h3').text()).toContain('This is your listing');
  });

  it('shows edit button when item belongs to user', async () => {
    const wrapper = mount(ProductPageView);

    wrapper.vm.isMyItem = true;
    await wrapper.vm.$nextTick();

    const editButton = wrapper.find('.edit-button');
    expect(editButton.exists()).toBe(true);
    expect(editButton.text()).toContain('Edit listing');
  });

  it('does not show edit button when item does not belong to user', async () => {
    const wrapper = mount(ProductPageView);

    wrapper.vm.isMyItem = false;
    await wrapper.vm.$nextTick();

    const editButton = wrapper.find('.edit-button');
    expect(editButton.exists()).toBe(false);
  });

  it('switches to edit mode when edit button is clicked', async () => {
    const wrapper = mount(ProductPageView);

    wrapper.vm.isMyItem = true;
    wrapper.vm.isEditing = false;

    wrapper.vm.mapItemResponseToAddItemRequest = (item) => ({
      name: item?.name || '',
      description: item?.description || '',
      price: item?.price || 0,
      category: item?.category || ''
    });

    await wrapper.vm.$nextTick();

    const editButton = wrapper.find('.edit-button');
    await editButton.trigger('click');

    expect(wrapper.vm.isEditing).toBe(true);
  });

  it('handles error display properly', async () => {
    const wrapper = mount(ProductPageView);

    wrapper.vm.errorMessage = 'Test error message';
    await wrapper.vm.$nextTick();

    const errorElement = wrapper.find('.error-state');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toContain('Test error message');
  });
});
