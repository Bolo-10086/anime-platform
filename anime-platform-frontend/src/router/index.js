import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import AnimeList from '../views/AnimeList.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Profile from '../views/Profile.vue'
import AnimeDetail from '../views/AnimeDetail.vue'
import AdminAnime from '../views/AdminAnime.vue'
import AdminStats from '../views/AdminStats.vue'
import NewsList from '../views/NewsList.vue'
import NewsDetail from '../views/NewsDetail.vue'
import AdminCategory from '../views/AdminCategory.vue'
import AdminTag from '../views/AdminTag.vue'
import AdminNews from '../views/AdminNews.vue'
import AdminUser from '../views/AdminUser.vue'
import AdminComment from '../views/AdminComment.vue'
import { getUser, hasToken } from '../utils/auth'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'Home', component: Home },
  { path: '/anime', name: 'AnimeList', component: AnimeList },
  { path: '/anime/:id', name: 'AnimeDetail', component: AnimeDetail },
  { path: '/news', name: 'NewsList', component: NewsList },
  { path: '/news/:id', name: 'NewsDetail', component: NewsDetail },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/profile', name: 'Profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/admin/stats', name: 'AdminStats', component: AdminStats, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/anime', name: 'AdminAnime', component: AdminAnime, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/category', name: 'AdminCategory', component: AdminCategory, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/tag', name: 'AdminTag', component: AdminTag, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/news', name: 'AdminNews', component: AdminNews, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/user', name: 'AdminUser', component: AdminUser, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/comment', name: 'AdminComment', component: AdminComment, meta: { requiresAuth: true, requiresAdmin: true } }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !hasToken()) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  if (to.meta.requiresAdmin) {
    const user = getUser()
    const roles = user && user.roles ? user.roles : []
    if (!roles.includes('ADMIN')) {
      next('/home')
      return
    }
  }
  next()
})

export default router
