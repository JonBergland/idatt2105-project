import type { UserLoginDTO, UserRegistrationDTO } from "@/models/user";
import { defineStore } from "pinia";
import { ref } from "vue";

import axiosInstance from "@/services/axiosService";

/**
 * Store for authenticating the user with login and singup
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuth: false,
    userData: null
  }),

  actions: {

    async checkIfAuth() {
      try {
        // Tests if one can retrieve data from backend
        this.isAuth = true

      } catch (error) {
        console.log("Authorization check failed: ", error);
        this.isAuth = false
      }
    },

    async login(user: UserLoginDTO) {
      try {
        const resp = await axiosInstance.post<UserLoginDTO>('/token/signin', user)
        console.log(resp);

        // Test to see if the user is authenticated
        if (resp) {
          this.checkIfAuth()
          if (this.userData) {
            return true
          }
        }

        return false
        
      } catch (error) {


      }
    },

    async signup(user: UserRegistrationDTO) {
      try {
        console.log("Sending user-info to backend: ", user);
        const resp = await axiosInstance.post<UserRegistrationDTO>('/token/signup', user)
        console.log("Response from backend: ", resp);

        if (resp) {
          this.checkIfAuth()

        if (this.userData) {
            return true
          }
        }
        return false

      } catch (error) {
        console.log("Error when signing up to Yard: ", error);
      }
    }

  }
})
