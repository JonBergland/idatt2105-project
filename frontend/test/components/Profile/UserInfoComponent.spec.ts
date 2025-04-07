import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import UserInfoComponent from '@/components/Profile/UserInfoComponent.vue'
import * as stringVerificationUtils from '@/utils/stringVerificationUtils'

describe('UserInfoComponent', () => {
  const defaultProps = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    countryCode: 47,
    phoneNumber: 12345678,
    address: '123 Main St',
    city: 'Oslo',
    postalCode: 150,
    isEditing: false
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders correctly in view mode', () => {
    const wrapper = mount(UserInfoComponent, {
      props: defaultProps
    })

    expect(wrapper.text()).toContain(defaultProps.firstName)
    expect(wrapper.text()).toContain(defaultProps.lastName)
    expect(wrapper.text()).toContain(defaultProps.email)
    expect(wrapper.text()).toContain(defaultProps.phoneNumber)

    expect(wrapper.find('input#firstName').exists()).toBe(false)
  })

  it('renders correctly in edit mode', () => {
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    expect(wrapper.find('input#firstName').exists()).toBe(true)
    expect(wrapper.find('input#lastName').exists()).toBe(true)
    expect(wrapper.find('input#email').exists()).toBe(true)
    expect(wrapper.find('input#phoneNumber').exists()).toBe(true)
  })

  it('pre-fills form fields with provided data', async () => {
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    expect(wrapper.find('input#firstName').element.value).toBe(defaultProps.firstName)
    expect(wrapper.find('input#lastName').element.value).toBe(defaultProps.lastName)
    expect(wrapper.find('input#email').element.value).toBe(defaultProps.email)
    expect(wrapper.find('input#countryCode').element.value).toBe(defaultProps.countryCode.toString())
  })

  it('validates form fields correctly', async () => {
    const verifyLettersSpy = vi.spyOn(stringVerificationUtils, 'verifyStringForLetters')
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await wrapper.find('input#firstName').setValue('Jane')

    expect(verifyLettersSpy).toHaveBeenCalledWith('Jane')
  })

  it('shows validation errors when fields are invalid', async () => {
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    const firstNameInput = wrapper.find('input#firstName')

    await firstNameInput.setValue('123')
    await firstNameInput.trigger('input')

    expect(wrapper.find('.error-msg').exists()).toBe(true)
  })

  it('emits update events when form values change', async () => {
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await wrapper.vm.$nextTick()

    const firstNameInput = wrapper.find('input#firstName')
    await firstNameInput.setValue('Jane')
    await firstNameInput.trigger('input')

    await wrapper.vm.$nextTick();

    expect(wrapper.vm.validForm).toBeTruthy()

    expect(wrapper.emitted('update:firstName')).toBeTruthy()
    expect(wrapper.emitted('update:firstName')[0]).toEqual(['Jane'])
    expect(wrapper.emitted('update')).toBeTruthy()

  })

  it('formats countryCode correctly when entering values', async () => {
    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await wrapper.find('input#countryCode').setValue('47')
    await wrapper.find('input#countryCode').trigger('input')

    expect(wrapper.find('input#countryCode').element.value).toBe('+47')
  })

  it('validates phone number field correctly', async () => {
    const verifyNumbersSpy = vi.spyOn(stringVerificationUtils, 'verifyStringForNumbers')
    verifyNumbersSpy.mockClear()

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    const phoneNumberInput = wrapper.find('input#phoneNumber')
    await phoneNumberInput.setValue('87654321')
    await phoneNumberInput.trigger('input')

    expect(verifyNumbersSpy).toHaveBeenCalledWith('87654321')
  })

  // Testing address field (line 169)
  it('validates address field correctly', async () => {
    const verifyNotEmptySpy = vi.spyOn(stringVerificationUtils, 'verifyStringNotEmpty')
    verifyNotEmptySpy.mockClear()

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    const addressInput = wrapper.find('input#address')
    await addressInput.setValue('New Street 42')
    await addressInput.trigger('input')

    expect(verifyNotEmptySpy).toHaveBeenCalledWith('New Street 42')
  })

  // Testing postal code field (line 176)
  it('validates postal code field correctly', async () => {
    const verifyNumbersSpy = vi.spyOn(stringVerificationUtils, 'verifyStringForNumbers')
    verifyNumbersSpy.mockClear()

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    const postalCodeInput = wrapper.find('input#postalCode')
    await postalCodeInput.setValue('12345')
    await postalCodeInput.trigger('input')

    expect(verifyNumbersSpy).toHaveBeenCalledWith('12345')
  })

  // Testing city field (line 185)
  it('validates city field correctly', async () => {
    const verifyNotEmptySpy = vi.spyOn(stringVerificationUtils, 'verifyStringNotEmpty')
    verifyNotEmptySpy.mockClear()

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    const cityInput = wrapper.find('input#city')
    await cityInput.setValue('New York')
    await cityInput.trigger('input')

    expect(verifyNotEmptySpy).toHaveBeenCalledWith('New York')
  })

  // Testing error messages (lines 134, 198, 211)
  it('shows and hides error messages based on validation state', async () => {
    vi.spyOn(stringVerificationUtils, 'verifyStringForLetters').mockReturnValue(false)
    vi.spyOn(stringVerificationUtils, 'verifyStringForEmail').mockReturnValue(false)
    vi.spyOn(stringVerificationUtils, 'verifyStringForNumbers').mockReturnValue(false)
    vi.spyOn(stringVerificationUtils, 'verifyStringNotEmpty').mockReturnValue(false)

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    expect(wrapper.findAll('.error-msg').length).toBeGreaterThan(0)

    expect(wrapper.findAll('.error-msg')[0].text()).toBe('Only letters allowed.')

    const postalCodeErrorMsg = wrapper.findAll('.error-msg').find(
      msg => msg.text().includes('Please enter a valid postal code')
    )
    expect(postalCodeErrorMsg).toBeTruthy()

    const cityErrorMsg = wrapper.findAll('.error-msg').find(
      msg => msg.text().includes('City cannot be empty')
    )
    expect(cityErrorMsg).toBeTruthy()

    vi.clearAllMocks()
    vi.spyOn(stringVerificationUtils, 'verifyStringForLetters').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringForEmail').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringForNumbers').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringNotEmpty').mockReturnValue(true)

    const newWrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await newWrapper.vm.$nextTick()

    expect(newWrapper.findAll('.error-msg').length).toBe(0)
  })

  it('emits update events for all fields when valid form is submitted', async () => {
    vi.spyOn(stringVerificationUtils, 'verifyStringForLetters').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringForEmail').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringForNumbers').mockReturnValue(true)
    vi.spyOn(stringVerificationUtils, 'verifyStringNotEmpty').mockReturnValue(true)

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await wrapper.vm.$nextTick()

    await wrapper.find('input#firstName').setValue('Jane')
    await wrapper.find('input#lastName').setValue('Smith')
    await wrapper.find('input#email').setValue('jane.smith@example.com')
    await wrapper.find('input#countryCode').setValue('1')
    await wrapper.find('input#phoneNumber').setValue('5551234')
    await wrapper.find('input#address').setValue('456 Other St')
    await wrapper.find('input#postalCode').setValue('54321')
    await wrapper.find('input#city').setValue('Chicago')

    await wrapper.find('input#firstName').trigger('input')
    await wrapper.find('input#lastName').trigger('input')
    await wrapper.find('input#email').trigger('input')
    await wrapper.find('input#countryCode').trigger('input')
    await wrapper.find('input#phoneNumber').trigger('input')
    await wrapper.find('input#address').trigger('input')
    await wrapper.find('input#postalCode').trigger('input')
    await wrapper.find('input#city').trigger('input')

    await wrapper.vm.$nextTick()

    expect(wrapper.vm.validForm).toBeTruthy()

    expect(wrapper.emitted('update:firstName')).toBeTruthy()
    expect(wrapper.emitted('update:lastName')).toBeTruthy()
    expect(wrapper.emitted('update:email')).toBeTruthy()
    expect(wrapper.emitted('update:countryCode')).toBeTruthy()
    expect(wrapper.emitted('update:phoneNumber')).toBeTruthy()
    expect(wrapper.emitted('update:address')).toBeTruthy()
    expect(wrapper.emitted('update:postalCode')).toBeTruthy()
    expect(wrapper.emitted('update:city')).toBeTruthy()

    const lastIndex = (arr: any[]) => arr.length - 1

    expect(wrapper.emitted('update:firstName')![lastIndex(wrapper.emitted('update:firstName')!)]).toEqual(['Jane'])
    expect(wrapper.emitted('update:lastName')![lastIndex(wrapper.emitted('update:lastName')!)]).toEqual(['Smith'])
    expect(wrapper.emitted('update:email')![lastIndex(wrapper.emitted('update:email')!)]).toEqual(['jane.smith@example.com'])
    expect(wrapper.emitted('update:countryCode')![lastIndex(wrapper.emitted('update:countryCode')!)]).toEqual([1])
    expect(wrapper.emitted('update:phoneNumber')![lastIndex(wrapper.emitted('update:phoneNumber')!)]).toEqual([5551234])
    expect(wrapper.emitted('update:address')![lastIndex(wrapper.emitted('update:address')!)]).toEqual(['456 Other St'])
    expect(wrapper.emitted('update:postalCode')![lastIndex(wrapper.emitted('update:postalCode')!)]).toEqual([54321])
    expect(wrapper.emitted('update:city')![lastIndex(wrapper.emitted('update:city')!)]).toEqual(['Chicago'])

    expect(wrapper.emitted('update')).toBeTruthy()
    expect(wrapper.emitted('update')![wrapper.emitted('update')!.length - 1]).toEqual([true])
  })

  it('does not emit individual update events when form validation fails', async () => {
    vi.spyOn(stringVerificationUtils, 'verifyStringForLetters').mockReturnValue(false)

    const wrapper = mount(UserInfoComponent, {
      props: {
        ...defaultProps,
        isEditing: true
      }
    })

    await wrapper.find('input#firstName').setValue('123')
    await wrapper.find('input#firstName').trigger('input')

    await wrapper.vm.$nextTick()

    expect(wrapper.vm.validForm).toBeFalsy()

    expect(wrapper.emitted('update:firstName')).toBeFalsy()

    expect(wrapper.emitted('update')).toBeTruthy()
    expect(wrapper.emitted('update')[0]).toEqual([false])
  })
})
