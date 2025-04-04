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

