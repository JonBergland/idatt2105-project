/**
 * Represents a user in the application
 */
export interface User {
  id?: number;
  email: string;
  firstName: string;
  lastName: string;
  landCode: string;
  phoneNr: string;
  password?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Data transfer object for user registration
 */
export interface UserRegistrationDTO {
  email: string;
  firstName: string;
  lastName: string;
  landCode: string;
  phoneNr: string;
  password: string;
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
    firstName: formData.firstName,
    lastName: formData.lastName,
    landCode: formData.landCode,
    phoneNr: formData.phoneNr,
    password: formData.password
  };
}
