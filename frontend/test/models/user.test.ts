import { describe, it, expect } from 'vitest';
import { User, UserRegistrationDTO, UserLoginDTO } from '@/models/user';

describe('User Interface', () => {
    it('should have all required properties', () => {
        const user: User = {
            userID: 1,
            email: 'test@example.com',
            name: 'John',
            surname: 'Doe',
            countryCode: 1,
            phoneNumber: 1234567890,
            role: 'admin',
            latitude: 40.7128,
            longitude: -74.0060,
            city: 'New York',
            postalCode: 10001,
            address: '123 Main St',
        };

        expect(user).toBeDefined();
        expect(user.userID).toBe(1);
        expect(user.email).toBe('test@example.com');
        expect(user.name).toBe('John');
        expect(user.surname).toBe('Doe');
        expect(user.countryCode).toBe(1);
        expect(user.phoneNumber).toBe(1234567890);
        expect(user.role).toBe('admin');
        expect(user.latitude).toBe(40.7128);
        expect(user.longitude).toBe(-74.0060);
        expect(user.city).toBe('New York');
        expect(user.postalCode).toBe(10001);
        expect(user.address).toBe('123 Main St');
    });
});

describe('UserRegistrationDTO Interface', () => {
    it('should have all required properties', () => {
        const registrationDTO: UserRegistrationDTO = {
            email: 'test@example.com',
            password: 'password123',
            name: 'John',
            surname: 'Doe',
            countryCode: 1,
            phoneNumber: 1234567890,
        };

        expect(registrationDTO).toBeDefined();
        expect(registrationDTO.email).toBe('test@example.com');
        expect(registrationDTO.password).toBe('password123');
        expect(registrationDTO.name).toBe('John');
        expect(registrationDTO.surname).toBe('Doe');
        expect(registrationDTO.countryCode).toBe(1);
        expect(registrationDTO.phoneNumber).toBe(1234567890);
    });
});

describe('UserLoginDTO Interface', () => {
    it('should have all required properties', () => {
        const loginDTO: UserLoginDTO = {
            email: 'test@example.com',
            password: 'password123',
        };

        expect(loginDTO).toBeDefined();
        expect(loginDTO.email).toBe('test@example.com');
        expect(loginDTO.password).toBe('password123');
    });
});
