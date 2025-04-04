/**
 * Represents a user in the application
 */
export interface User {
  email: string;
  name: string;
  surname: string;
  countryCode: number;
  phoneNumber: number;
  role: string;
}

/**
 * Data transfer object for user registration
 */
export interface UserRegistrationDTO {
  email: string;
  password: string;
  name: string;
  surname: string;
  countryCode: number;
  phoneNumber: number;
}

/**
 * Data transfer object for user login
 */
export interface UserLoginDTO {
  email: string;
  password: string;
}

/**
 * Creates a UserRegistrationDTO from form data
 * @param formData The data from the registration form
 * @returns        A UserRegistrationDTO object ready to be sent to the backend
 */
export function createRegistrationDTO(formData: any): UserRegistrationDTO {
  return {
    email: formData.email,
    name: formData.firstName,
    surname: formData.lastName,
    countryCode: formData.countryCode,
    phoneNumber: formData.phoneNumber,
    password: formData.password
  };
}
