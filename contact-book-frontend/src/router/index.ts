import { createRouter, createWebHistory } from 'vue-router'
import ContactsPage from '../components/pages/ContactsPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'contacts',
      component: ContactsPage
    },
    {
      path: '/groups',
      name: 'groups',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../components/pages/GroupsPage.vue')
    }
  ]
})

export default router
