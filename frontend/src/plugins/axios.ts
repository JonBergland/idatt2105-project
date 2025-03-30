import axios from 'axios'
import { useRouter } from 'vue-router'

/**
 * Creates a pre-configured Axios instance for making HTTP requests to the backend API.
 *
 * @constant
 * @type {AxiosInstance}
 *
 * @property {string} baseURL - The base URL for the backend API (`http://localhost:8080/api`).
 * @property {object} headers - Default headers for all requests, including `Content-Type: application/json`.
 * @property {boolean} withCredentials - Indicates whether cross-site Access-Control requests
 *                                        should be made using credentials such as cookies or authorization headers.
 *
 * @remarks
 * This instance is configured to communicate with the backend server running locally on port 8080.
 *
 * @example
 * ```typescript
 * axiosInstance.get('/example-endpoint')
 *   .then(response => console.log(response.data))
 *   .catch(error => console.error(error));
 * ```
 */
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // Backend url
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

let refreshTimeout

function scheduleTokenRefresh(expiresInMinutes = 5) {
  if (refreshTimeout) clearTimeout(refreshTimeout)

  const refreshTime = (expiresInMinutes * 60 - 30) * 1000

  refreshTimeout = setTimeout(async () => {
    // Add logic to generate new token
  }, refreshTime)
}

// Catches Unauthorized Access
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    const router = useRouter()

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      // TODO If userstore is valid, get new token
      try {
        //Get new token

        return axiosInstance(originalRequest)
      } catch (refreshError) {
        // TODO Clear user
        router.push('home')
        return Promise.reject(refreshError)
      }
    } else {
      // No user logged in to refresh with
      router.push('home')
    }

    return Promise.reject(error)
  }
)

export default axiosInstance
