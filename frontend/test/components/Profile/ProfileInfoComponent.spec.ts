import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import ProfileInfoComponent from '@/components/Profile/ProfileInfoComponent.vue';
import UserInfoComponent from '@/components/Profile/UserInfoComponent.vue';
import ProfileButtonsComponent from '@/components/Profile/ProfileButtonsComponent.vue';
import type { User } from '@/models/user';
import { nextTick } from 'vue';

// Mocking the child components to simplify testing
vi.mock('@/components/Profile/UserInfoComponent.vue', () => ({
  default: {
    name: 'UserInfoComponent',
    template: '<div class="user-info-mock"></div>',
    props: ['firstName', 'lastName', 'email', 'countryCode', 'phoneNumber', 'address', 'postalCode', 'city', 'isEditing'],
    emits: ['update', 'update:firstName', 'update:lastName', 'update:email', 'update:countryCode', 'update:phoneNumber', 'update:address', 'update:postalCode', 'update:city']
  }
}));

vi.mock('@/components/Profile/ProfileButtonsComponent.vue', () => ({
  default: {
    name: 'ProfileButtonsComponent',
    template: '<div class="profile-buttons-mock"></div>',
    props: ['editMode', 'logoutMode', 'activeSaveButton', 'errorMessage'],
    emits: ['editMode', 'logoutMode', 'save', 'logout', 'cancel']
  }
}));

describe('ProfileInfoComponent', () => {
  let mockUser: User;

  beforeEach(() => {
    mockUser = {
      userID: 31,
      email: 'john.doe@example.com',
      name: 'John',
      surname: 'Doe',
      countryCode: 47,
      phoneNumber: 12345678,
      role: 'ROLE_USER',
      latitude: 59.9139,
      longitude: 10.7522,
      city: 'Oslo',
      postalCode: 150,
      address: '123 Main St'
    };
  });

  it('renders correctly with user data', () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.profile-info-wrapper').exists()).toBe(true);
    expect(wrapper.find('.profile-image').exists()).toBe(true);
    expect(wrapper.find('#profile-image').exists()).toBe(true);
    expect(wrapper.find('.profile-details').exists()).toBe(true);
  });

  it('displays loading message when user is null', () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: null,
        errorMessage: ''
      },
      global: {
        stubs: {
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.find('.loading-message').exists()).toBe(true);
    expect(wrapper.find('.loading-message').text()).toBe('Loading user information...');
  });

  it('passes correct props to UserInfoComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          ProfileButtonsComponent: true
        }
      }
    });

    const userInfoComponent = wrapper.findComponent(UserInfoComponent);
    expect(userInfoComponent.props('firstName')).toBe(mockUser.name);
    expect(userInfoComponent.props('lastName')).toBe(mockUser.surname);
    expect(userInfoComponent.props('email')).toBe(mockUser.email);
    expect(userInfoComponent.props('countryCode')).toBe(mockUser.countryCode);
    expect(userInfoComponent.props('phoneNumber')).toBe(mockUser.phoneNumber);
    expect(userInfoComponent.props('address')).toBe(mockUser.address);
    expect(userInfoComponent.props('postalCode')).toBe(mockUser.postalCode);
    expect(userInfoComponent.props('city')).toBe(mockUser.city);
    expect(userInfoComponent.props('isEditing')).toBe(false);
  });

  it('passes correct props to ProfileButtonsComponent', () => {
    const errorMessage = 'Test error message';
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: errorMessage
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);
    expect(profileButtonsComponent.props('editMode')).toBe(false);
    expect(profileButtonsComponent.props('logoutMode')).toBe(false);
    expect(profileButtonsComponent.props('activeSaveButton')).toBe(true);
    expect(profileButtonsComponent.props('errorMessage')).toBe(errorMessage);
  });

  it('creates a local copy of user data on mount', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.vm.editableUser).toEqual(mockUser);

    const modifiedUser = { ...mockUser, name: 'Jane' };
    await wrapper.setProps({ user: modifiedUser });

    expect(wrapper.vm.editableUser).toEqual(modifiedUser);
  });

  it('toggles edit mode when handleEditMode is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.vm.isEditing).toBe(false);

    wrapper.vm.handleEditMode();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.isEditing).toBe(true);
    expect(wrapper.vm.isLoggingOut).toBe(false);

    const userInfoComponent = wrapper.findComponent(UserInfoComponent);
    expect(userInfoComponent.props('isEditing')).toBe(true);
  });

  it('toggles logout mode when handleLogoutMode is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.vm.isLoggingOut).toBe(false);

    wrapper.vm.handleLogoutMode();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.isLoggingOut).toBe(true);
    expect(wrapper.vm.isEditing).toBe(false);
  });

  it('emits saveUser event with current user data when handleSave is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    wrapper.vm.isEditing = true;
    await wrapper.vm.$nextTick();

    wrapper.vm.handleSave();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.isEditing).toBe(false);

    expect(wrapper.emitted('saveUser')).toBeTruthy();
    expect(wrapper.emitted('saveUser')![0][0]).toEqual(mockUser);
  });

  it('emits logoutUser event when handleLogout is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    wrapper.vm.isLoggingOut = true;
    await wrapper.vm.$nextTick();

    wrapper.vm.handleLogout();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.isLoggingOut).toBe(false);

    expect(wrapper.emitted('logoutUser')).toBeTruthy();
  });

  it('resets editable user data when handleCancel is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    wrapper.vm.editableUser = {
      ...mockUser,
      name: 'Changed Name',
      surname: 'Changed Surname'
    };
    wrapper.vm.isEditing = true;
    await wrapper.vm.$nextTick();

    wrapper.vm.handleCancel();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.isEditing).toBe(false);

    expect(wrapper.vm.editableUser).toEqual(mockUser);
  });

  it('updates validUser when handleUpdate is called', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true,
          ProfileButtonsComponent: true
        }
      }
    });

    expect(wrapper.vm.validUser).toBe(true);

    wrapper.vm.handleUpdate(false);
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.validUser).toBe(false);

    wrapper.vm.handleUpdate(true);
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.validUser).toBe(true);
  });

  it('responds to edit-mode event from ProfileButtonsComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);

    await profileButtonsComponent.vm.$emit('editMode');
    await nextTick();

    expect(wrapper.vm.isEditing).toBe(true);
  });

  it('responds to logout-mode event from ProfileButtonsComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);

    await profileButtonsComponent.vm.$emit('logoutMode');
    await nextTick();

    expect(wrapper.vm.isLoggingOut).toBe(true);
  });

  it('responds to save event from ProfileButtonsComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    wrapper.vm.isEditing = true;
    await nextTick();

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);
    await profileButtonsComponent.vm.$emit('save');
    await nextTick();

    expect(wrapper.vm.isEditing).toBe(false);

    expect(wrapper.emitted()).toHaveProperty('saveUser');
  });

  it('responds to logout event from ProfileButtonsComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);
    await profileButtonsComponent.vm.$emit('logout');
    await nextTick();

    expect(wrapper.emitted()).toHaveProperty('logoutUser');
  });

  it('responds to cancel event from ProfileButtonsComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          UserInfoComponent: true
        }
      }
    });

    wrapper.vm.isEditing = true;
    await nextTick();

    const profileButtonsComponent = wrapper.findComponent(ProfileButtonsComponent);
    await profileButtonsComponent.vm.$emit('cancel');
    await nextTick();

    expect(wrapper.vm.isEditing).toBe(false);
  });

  it('responds to update event from UserInfoComponent', async () => {
    const wrapper = mount(ProfileInfoComponent, {
      props: {
        user: mockUser,
        errorMessage: ''
      },
      global: {
        stubs: {
          ProfileButtonsComponent: true
        }
      }
    });

    const userInfoComponent = wrapper.findComponent(UserInfoComponent);
    await userInfoComponent.vm.$emit('update', false);
    await nextTick();

    expect(wrapper.vm.validUser).toBe(false);
  });
});
