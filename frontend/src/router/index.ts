import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LogIn/LogInView.vue'
import SignUpView from '@/views/SignUp/SignUpView.vue'
import ProductPageView from '@/views/ProductPage/ProductPageView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUpView,
    },
    {
      path: '/product/:productId',
      name: 'product',
      component: ProductPageView,
      props: true
    },
  ],
})

export default router
