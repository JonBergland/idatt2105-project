import { describe, it, expect, vi, afterEach, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils';
import Header from '../MainComponents/Header.vue';
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  { path: '/', name: 'home', component: {} },
  { path: '/listing', name: 'listing', component: {} },
  { path: '/favorites', name: 'favorites', component: {} },
  { path: '/messages', name: 'messages', component: {} },
  { path: '/profile', name: 'profile', component: {} },
  { path: '/login', name: 'login', component: {} },
  { path: '/signup', name: 'signup', component: {} },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

vi.mock('@/assets/logos/logo.svg', () => ({ default: '/mocked-logo.svg' }));
vi.mock('@/assets/logos/logo3.svg', () => ({ default: '/mocked-logo3.svg' }));

vi.mock('@/assets/icons/user-selected.svg', () => ({ default: '/mocked-user-selected-icon.svg' }));
vi.mock('@/assets/icons/message-notification.svg', () => ({ default: '/mocked-notification-icon.svg' }));

describe('Header.vue', () => {

  it('renders the logo correctly', () => {
    const wrapper = mount(Header, {
      global: {
        plugins: [router],
      },
    });

    const desktopLogo = wrapper.find('#desktopLogo');
    const mobileLogo = wrapper.find('#mobileLogo');

    expect(desktopLogo.exists()).toBe(true);
    expect(desktopLogo.attributes('src')).toBe('/mocked-logo.svg');
    expect(mobileLogo.exists()).toBe(true);
    expect(mobileLogo.attributes('src')).toBe('/mocked-logo3.svg');
  });

  it('renders the "Log in" and "Sign up" buttons when not logged in', () => {
    const wrapper = mount(Header, {
      global: {
        plugins: [router],
        mocks: {
          hasToken: false,
        },
      },
    });

    const loginButton = wrapper.find('.loginButton');
    const signupButton = wrapper.find('.signupButton');

    expect(loginButton.exists()).toBe(true);
    expect(signupButton.exists()).toBe(true);

    expect(loginButton.text()).toBe('Log in');
    expect(signupButton.text()).toBe('Sign up');

    expect(loginButton.attributes('href')).toBe('/login');
    expect(signupButton.attributes('href')).toBe('/signup');
  });

  it('renders the navigation links when logged in', async () => {
    const wrapper = mount(Header, {
      global: {
        plugins: [router],
        mocks: {
          hasToken: true,
        },
      },
    });

  const links = wrapper.findAll('.routerLink');

  expect(links.length).toBe(4);
  expect(links[0].attributes('href')).toBe('/listing');
  expect(links[1].attributes('href')).toBe('/favorites');
  expect(links[2].attributes('href')).toBe('/messages');
  expect(links[3].attributes('href')).toBe('/profile');
  });

  it('applies the "active" class to the correct link based on the current route', async () => {
    await router.push('/listing');
    await router.isReady();

    const wrapper = mount(Header, {
      global: {
        plugins: [router],
        mocks: {
          hasToken: true,
        },
      },
    });

    const listingLink = wrapper.find('.routerLink.active');
    expect(listingLink.exists()).toBe(true);
    expect(listingLink.attributes('href')).toBe('/listing');
  });

  it('renders the selected icon version based on the current route', async () => {
    await router.push('/profile');
    await router.isReady();

    const wrapper = mount(Header, {
      global: {
        plugins: [router],
        mocks: {
          hasToken: true,
        },
      },
    });

    const listingLink = wrapper.find('.routerLink.active');
    const selectedIcon = listingLink.find('img[src="/mocked-user-selected-icon.svg"]');
    expect(selectedIcon.exists()).toBe(true);
    expect(selectedIcon.attributes('src')).toBe('/mocked-user-selected-icon.svg');
  });

  it('renders the notification icon when there are notifications', () => {
    const wrapper = mount(Header, {
      global: {
        plugins: [router],
        mocks: {
          hasToken: true,
          hasNotification: true,
        },
      },
    });

    const notificationIcon = wrapper.find('img[src="/mocked-notification-icon.svg"]');
    expect(notificationIcon.exists()).toBe(true);
  });
});
