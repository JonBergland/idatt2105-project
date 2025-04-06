import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ProfileButtonsComponent from '@/components/Profile/ProfileButtonsComponent.vue'

describe('ProfileButtonsComponent', () => {
  const defaultProps = {
    editMode: false,
    logoutMode: false
  }

  it('renders correctly with default props', () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: defaultProps
    })

    expect(wrapper.exists()).toBe(true)
    expect(wrapper.find('.profile-buttons').exists()).toBe(true)
  })

  it('does not show buttons when in default state', () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: defaultProps
    })

    expect(wrapper.find('.edit-info-button').exists()).toBe(true)
    expect(wrapper.find('.logout-button').exists()).toBe(true)
    expect(wrapper.find('.save-info').exists()).toBe(false)
    expect(wrapper.find('.cancel-button').exists()).toBe(false)
    expect(wrapper.find('.logout').exists()).toBe(false)
  })

  it('emits editMode event when edit button is clicked', async () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: defaultProps,
    })

    await wrapper.find('.edit-info-button').trigger('click')
    expect(wrapper.emitted('editMode')).toBeTruthy()
    expect(wrapper.emitted('editMode')).toHaveLength(1)
  })

  it('emits logoutMode event when logout button is clicked', async () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: defaultProps,
    })

    await wrapper.find('.logout-button').trigger('click')
    expect(wrapper.emitted('logoutMode')).toBeTruthy()
    expect(wrapper.emitted('logoutMode')).toHaveLength(1)
  })

  it('conditionally renders based on editMode prop', () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: true,
        logoutMode: false
      }
    })

    expect(wrapper.find('.save-info').exists()).toBe(true)
    expect(wrapper.find('.cancel-button').exists()).toBe(true)
  })

  it('conditionally renders based on logoutMode prop', () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: false,
        logoutMode: true
      }
    })

    expect(wrapper.find('.logout').exists()).toBe(true)
    expect(wrapper.find('.cancel-button').exists()).toBe(true)

  })

  it('emits save event when save button is clicked', async () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: true,
        logoutMode: false
      },
    })

    await wrapper.find('.save-info').trigger('click')
    expect(wrapper.emitted('save')).toBeTruthy()
    expect(wrapper.emitted('save')).toHaveLength(1)
  })

  it('emits cancel event when cancel button in editMode is clicked', async () => {

    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: true,
        logoutMode: false
      },
    })

    await wrapper.find('.cancel-button').trigger('click')
    expect(wrapper.emitted('cancel')).toBeTruthy()
    expect(wrapper.emitted('cancel')).toHaveLength(1)
  })

  it('emits logout event when logout confirmation button is clicked', async () => {
    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: false,
        logoutMode: true
      },
    })

    await wrapper.find('.logout').trigger('click')
    expect(wrapper.emitted('logout')).toBeTruthy()
    expect(wrapper.emitted('logout')).toHaveLength(1)
  })

  it('emits cancel event when cancel button in logoutMode is clicked', async () => {

    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: false,
        logoutMode: true
      },
    })

    await wrapper.find('.cancel-button').trigger('click')
    expect(wrapper.emitted('cancel')).toBeTruthy()
    expect(wrapper.emitted('cancel')).toHaveLength(1)
  })

  it('conditionally renders based on editMode and logoutMode prop', async () => {

    const wrapper = mount(ProfileButtonsComponent, {
      props: {
        editMode: true,
        logoutMode: true
      },
    })

    expect(wrapper.find('.profile-buttons').element.children.length).toBe(0)
  })
})
