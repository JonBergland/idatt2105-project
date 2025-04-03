import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home/HomeView.vue'
import LoginView from '@/views/LogIn/LogInView.vue'
import SignUpView from '@/views/SignUp/SignUpView.vue'
import ResultView from '@/views/Result/ResultView.vue'

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
      path: '/result',
      name: 'result',
      component: ResultView,
    },
  ],
})

export default router
