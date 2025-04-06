import { mount } from '@vue/test-utils';
import { setActivePinia, createPinia } from 'pinia';
import { createRouter, createWebHistory } from 'vue-router';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import ResultView from '@/views/Result/ResultView.vue';
import { useResultStore } from '@/stores/resultStore';
import { nextTick } from 'vue';

vi.mock('@/components/Result/ToggleGroup.vue', () => ({
  default: {
    template: '<div class="toggle-group-stub"></div>',
    props: ['names', 'initialSelected', 'allowDeselect', 'direction', 'autoSelectFirst', 'label']
  }
}));

vi.mock('@/components/Home/SearchBar.vue', () => ({
  default: {
    template: '<div class="search-bar-stub"></div>',
    props: ['modelValue'],
    emits: ['search-input', 'update:modelValue']
  }
}));

vi.mock('@/components/Home/ItemGroup.vue', () => ({
  default: {
    template: '<div class="item-group-stub"></div>',
    props: ['items', 'mode']
  }
}));

vi.mock('@/components/Result/PriceFilter.vue', () => ({
  default: {
    template: '<div class="price-filter-stub"></div>'
  }
}));

describe('ResultView.vue', () => {
  let router;
  let store;

  beforeEach(() => {
    setActivePinia(createPinia());

    router = createRouter({
      history: createWebHistory(),
      routes: [
        { path: '/result', name: 'result', component: ResultView },
        { path: '/product', name: 'product', component: { template: '<div></div>' } }
      ]
    });

    store = useResultStore();

    vi.spyOn(store, 'fetchItems').mockImplementation(() => Promise.resolve());
    vi.spyOn(store, 'fetchCategories').mockImplementation(() => Promise.resolve());
    vi.spyOn(store, 'loadMoreItems').mockImplementation(() => Promise.resolve());

    vi.spyOn(window, 'addEventListener').mockImplementation(() => {});
    vi.spyOn(window, 'scrollY', 'get').mockReturnValue(0);
    vi.spyOn(window, 'innerHeight', 'get').mockReturnValue(800);
    Object.defineProperty(document.documentElement, 'scrollHeight', { value: 1000 });
  });

  describe('getSortLabel', () => {
    it('returns correct display labels for sort values', async () => {
      await router.push('/result');
      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      const { getSortLabel } = wrapper.vm as any;

      expect(getSortLabel('published_DESC')).toBe('New');
      expect(getSortLabel('price_ASC')).toBe('Price Up');
      expect(getSortLabel('price_DESC')).toBe('Price Down');
      expect(getSortLabel(null)).toBe('published_DESC');
    });
  });

  describe('URL parameter handling', () => {
    it('initializes filters from URL parameters', async () => {
      await router.push('/result?search=laptop&category=Electronics&sort=price_ASC&minPrice=100&maxPrice=1000');

      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      await wrapper.vm.$nextTick();

      const { itemsRequest } = wrapper.vm as any;
      expect(itemsRequest.searchWord).toBe('laptop');
      expect(itemsRequest.category).toBe('Electronics');
      expect(itemsRequest.sort).toBe('price_ASC');
      expect(itemsRequest.priceMinMax).toEqual([100, 1000]);
    });

    it('calls fetchItems with correct parameters on mount', async () => {
      await router.push('/result?category=Books');

      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      await wrapper.vm.$nextTick();

      expect(store.fetchItems).toHaveBeenCalledWith(
        expect.objectContaining({
          category: 'Books'
        })
      );
    });
  });

  describe('filter handlers', () => {
    it('handleSearch updates searchWord in itemsRequest', async () => {
      await router.push('/result');
      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      const { handleSearch, itemsRequest } = wrapper.vm as any;

      handleSearch('test query');
      expect(itemsRequest.searchWord).toBe('test query');

      handleSearch('');
      expect(itemsRequest.searchWord).toBeNull();
    });

    it('handleCategoryClick updates category in itemsRequest', async () => {
      await router.push('/result');
      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      const { handleCategoryClick, itemsRequest } = wrapper.vm as any;

      handleCategoryClick('Electronics');
      expect(itemsRequest.category).toBe('Electronics');
    });

    it('handlePriceRangeUpdated updates priceMinMax in itemsRequest', async () => {
      await router.push('/result');
      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      const { handlePriceRangeUpdated, itemsRequest } = wrapper.vm as any;

      handlePriceRangeUpdated({ min: 50, max: 200 });
      expect(itemsRequest.priceMinMax).toEqual([50, 200]);

      handlePriceRangeUpdated({ min: null, max: 300 });
      expect(itemsRequest.priceMinMax).toEqual([null, 300]);
    });

    it('handleSort updates sort in itemsRequest', async () => {
      await router.push('/result');
      const wrapper = mount(ResultView, {
        global: {
          plugins: [router]
        }
      });

      const { handleSort, itemsRequest } = wrapper.vm as any;

      handleSort('Price Up');
      expect(itemsRequest.sort).toBe('price_ASC');

      handleSort('Price Down');
      expect(itemsRequest.sort).toBe('price_DESC');

      handleSort('New');
      expect(itemsRequest.sort).toBe('published_DESC');
    });
  });

  it('loadMoreItems increments page and calls store method', async () => {
    await router.push('/result');
    const wrapper = mount(ResultView, {
      global: {
        plugins: [router]
      }
    });

    const { loadMoreItems } = wrapper.vm as any;

    expect(wrapper.vm.currentPage).toBe(0);

    vi.spyOn(store, 'loadMoreItems').mockImplementation(() => {
      return Promise.resolve();
    });

    await loadMoreItems();
    await nextTick();

    expect(wrapper.vm.currentPage).toBe(1);
    expect(store.loadMoreItems).toHaveBeenCalledWith(
      expect.objectContaining({
        segmentOffset: [1, 6]
      })
    );
  });

  it('sets hasMoreItems to false when fewer items returned', async () => {
    await router.push('/result');
    const wrapper = mount(ResultView, {
      global: {
        plugins: [router]
      }
    });

    vi.spyOn(store, 'loadMoreItems').mockImplementation(() => {
      store.newItemsCount = 3;
      return Promise.resolve();
    });

    const { loadMoreItems } = wrapper.vm as any;

    expect(wrapper.vm.hasMoreItems).toBe(true);

    await loadMoreItems();
    await nextTick();

    expect(wrapper.vm.hasMoreItems).toBe(false);
  });
});
