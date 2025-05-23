import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import SearchBar from '@/components/Home/SearchBar.vue';

describe('SearchBar.vue', () => {
  it('renders the input and button correctly', () => {
    const wrapper = mount(SearchBar);

    const input = wrapper.find('input');
    expect(input.exists()).toBe(true);
    expect(input.attributes('placeholder')).toBe('Search for anything...');

    const button = wrapper.find('button');
    expect(button.exists()).toBe(true);

    const img = button.find('img');
    expect(img.exists()).toBe(true);
    expect(img.attributes('alt')).toBe('Search');
  });

  it('updates the searchInput value when typing in the input field', async () => {
    const wrapper = mount(SearchBar);

    const input = wrapper.find('input');
    await input.setValue('Playstation');

    expect((input.element as HTMLInputElement).value).toBe('Playstation');
  });

  it('emits "search-triggered" with the correct payload when the button is clicked', async () => {
    const wrapper = mount(SearchBar);

    const input = wrapper.find('input');
    const button = wrapper.find('button');

    await input.setValue('Playstation');

    await button.trigger('click');

    expect(wrapper.emitted('search-triggered')).toBeTruthy();
    expect(wrapper.emitted('search-triggered')![0]).toEqual(['Playstation']);
  });

  it('emits "search-triggered" with an empty string if the input is empty', async () => {
    const wrapper = mount(SearchBar);

    const button = wrapper.find('button');

    await button.trigger('click');

    expect(wrapper.emitted('search-triggered')).toBeTruthy();
    expect(wrapper.emitted('search-triggered')![0]).toEqual(['']);
  });

  it('emits "search-input" when typing in the input field', async () => {
    const wrapper = mount(SearchBar);

    const input = wrapper.find('input');
    await input.setValue('Play');
    await input.trigger('input');

    expect(wrapper.emitted('search-input')).toBeTruthy();
    expect(wrapper.emitted('search-input')![0]).toEqual(['Play']);
  });

  it('emits "search-triggered" when pressing Enter key', async () => {
    const wrapper = mount(SearchBar);

    const input = wrapper.find('input');
    await input.setValue('Nintendo');
    await input.trigger('keyup.enter');

    expect(wrapper.emitted('search-triggered')).toBeTruthy();
    expect(wrapper.emitted('search-triggered')![0]).toEqual(['Nintendo']);
  });
});
