import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/index',
  },
  {
    path: '/index',
    name: 'Home',
    component: () => import('@/views/home.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register.vue'),
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/index',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
