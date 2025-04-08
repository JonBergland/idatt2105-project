import { mount, flushPromises } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ItemForm from '@/components/NewListing/ItemForm.vue';

vi.mock('@/stores/itemStore.ts', () => ({
  useItemStore: () => ({
    categories: ['Electronics', 'Clothing', 'Books'],
    categoriesError: null,
    fetchCategories: vi.fn()
  })
}));

vi.mock('@/components/Result/ToggleGroup.vue', () => ({
  default: {
    name: 'ToggleGroup',
    props: ['names', 'autoSelectFirst', 'direction', 'allowDeselect'],
    template: '<div data-testid="toggle-group"></div>',
    emits: ['toggle-selected']
  }
}));

describe('ItemForm', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(ItemForm, {
      global: {
        stubs: {
          ToggleGroup: true
        }
      }
    });
  });

  it('renders correctly', () => {
    expect(wrapper.find('.item-form-container').exists()).toBe(true);
    expect(wrapper.find('#name').exists()).toBe(true);
    expect(wrapper.find('#description').exists()).toBe(true);
    expect(wrapper.find('#price').exists()).toBe(true);
    expect(wrapper.find('#submit-button').exists()).toBe(true);
  });

  it('initializes with empty form data', () => {
    const formData = wrapper.vm.item;
    expect(formData.name).toBe('');
    expect(formData.description).toBe('');
    expect(formData.price).toBe(0);
    expect(formData.category).toBe('');
  });

  it('validates name field', async () => {
    await wrapper.find('#name').setValue('');
    await wrapper.find('#name').trigger('blur');
    expect(wrapper.vm.nameTouched).toBe(true);
    expect(wrapper.vm.validName).toBe(false);
    expect(wrapper.find('.error-msg').exists()).toBe(true);

    await wrapper.find('#name').setValue('ab');
    expect(wrapper.vm.validName).toBe(false);

    await wrapper.find('#name').setValue('Valid Name');
    expect(wrapper.vm.validName).toBe(true);
  });

  it('validates description field', async () => {
    await wrapper.find('#description').setValue('');
    await wrapper.find('#description').trigger('blur');
    expect(wrapper.vm.descriptionTouched).toBe(true);
    expect(wrapper.vm.validDescription).toBe(false);

    await wrapper.find('#description').setValue('This is a valid description');
    expect(wrapper.vm.validDescription).toBe(true);
  });

  it('validates price field', async () => {
    await wrapper.find('#price').setValue(-5);
    await wrapper.find('#price').trigger('blur');
    expect(wrapper.vm.priceTouched).toBe(true);
    expect(wrapper.vm.validPrice).toBe(false);

    await wrapper.find('#price').setValue(100);
    expect(wrapper.vm.validPrice).toBe(true);
  });

  it('disables submit button when form is invalid', async () => {
    expect(wrapper.find('#submit-button').attributes('disabled')).toBeDefined();

    await wrapper.find('#name').setValue('Test Item');
    await wrapper.find('#description').setValue('A valid description');
    await wrapper.find('#price').setValue(50);

    await wrapper.vm.handleCategoryClick('Electronics');

    await flushPromises();
    expect(wrapper.vm.validForm).toBe(true);
    expect(wrapper.find('#submit-button').attributes('disabled')).toBeUndefined();
  });

  it('handles category selection', async () => {
    expect(wrapper.vm.item.category).toBe('');

    await wrapper.vm.handleCategoryClick('Books');

    expect(wrapper.vm.item.category).toBe('Books');
    expect(wrapper.vm.categoryTouched).toBe(true);
  });

  it('emits submit event with formatted data when form is valid', async () => {
    await wrapper.find('#name').setValue('Test Item');
    await wrapper.find('#description').setValue('A valid description');
    await wrapper.find('#price').setValue('75.5');
    await wrapper.vm.handleCategoryClick('Clothing');

    await wrapper.find('form').trigger('submit.prevent');

    const emitted = wrapper.emitted('submit');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toEqual({
      name: 'Test Item',
      description: 'A valid description',
      price: 75.5,
      category: 'Clothing'
    });
  });

  it('does not emit submit event when form is invalid', async () => {
    await wrapper.find('#name').setValue('');
    await wrapper.find('form').trigger('submit.prevent');

    expect(wrapper.emitted('submit')).toBeFalsy();
  });

  it('sets error label when form submission fails', async () => {
    const errorLabel = document.createElement('div');
    wrapper.vm.errorLabelEl = errorLabel;

    await wrapper.find('form').trigger('submit.prevent');

    expect(errorLabel.textContent).toContain('Submission failed');
  });

  it('fetches categories on mount', () => {
    const itemStore = wrapper.vm.itemStore;
    expect(itemStore.fetchCategories).toHaveBeenCalled();
  });
});
