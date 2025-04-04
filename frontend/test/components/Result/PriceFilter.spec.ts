import { describe, it, expect} from 'vitest'
import { mount } from '@vue/test-utils';
import PriceFilter from '@/components/Result/PriceFilter.vue';

describe('PriceFilter.vue', () => {
  it('emits the correct price range when inputs are updated', async () => {
    const wrapper = mount(PriceFilter);

    const minInput = wrapper.find('input[placeholder="Min"]');
    const maxInput = wrapper.find('input[placeholder="Max"]');

    await minInput.setValue(100);
    expect(wrapper.emitted('price-range-updated')).toBeTruthy();
    expect(wrapper.emitted('price-range-updated')?.[0]).toEqual([{ min: 100, max: null }]);

    await maxInput.setValue(500);
    expect(wrapper.emitted('price-range-updated')?.[1]).toEqual([{ min: 100, max: 500 }]);
  });

  it('renders the correct placeholders and labels', () => {
    const wrapper = mount(PriceFilter);

    expect(wrapper.find('input[placeholder="Min"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Max"]').exists()).toBe(true);

    expect(wrapper.text()).toContain('From kr');
    expect(wrapper.text()).toContain('To kr');
  });

  it('handles empty inputs correctly', async () => {
    const wrapper = mount(PriceFilter);

    const minInput = wrapper.find('input[placeholder="Min"]');
    const maxInput = wrapper.find('input[placeholder="Max"]');

    // Simulate clearing the inputs
    await minInput.setValue('');
    await maxInput.setValue('');

    // Ensure the emitted event reflects null values
    expect(wrapper.emitted('price-range-updated')?.[0]).toEqual([{ min: null, max: null }]);
    expect(wrapper.emitted('price-range-updated')?.[1]).toEqual([{ min: null, max: null }]);
  });

  it('shows an error message when min is greater than max', async () => {
    const wrapper = mount(PriceFilter);

    const minInput = wrapper.find('input[placeholder="Min"]');
    const maxInput = wrapper.find('input[placeholder="Max"]');

    // Simulate entering an invalid range
    await minInput.setValue(300);
    await maxInput.setValue(200);

    // Check that the error message is displayed
    expect(wrapper.find('.error-message').text()).toBe('Max price must be lower than min price');
  });

  it('does not show an error message for a valid range', async () => {
    const wrapper = mount(PriceFilter);

    const minInput = wrapper.find('input[placeholder="Min"]');
    const maxInput = wrapper.find('input[placeholder="Max"]');

    // Simulate entering a valid range
    await minInput.setValue(100);
    await maxInput.setValue(200);

    // Check that no error message is displayed
    expect(wrapper.find('.error-message').exists()).toBe(false);
  });

  it('shows an error message when a negative number is entered', async () => {
    const wrapper = mount(PriceFilter);

    const minInput = wrapper.find('input[placeholder="Min"]');

    // Simulate entering a negative number
    await minInput.setValue(-50);

    // Check that the error message is displayed
    expect(wrapper.find('.error-message').text()).toBe('Number cant be negative');
  });
});
