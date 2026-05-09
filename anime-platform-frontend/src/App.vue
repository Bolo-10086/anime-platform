<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="brand" @click="$router.push('/home')">
        <span class="brand-mark">A</span>
        <span>AnimeHub</span>
      </div>
      <div class="header-right">
        <el-menu mode="horizontal" router :default-active="$route.path" background-color="#111827" text-color="#d8dee9" active-text-color="#ff9f1c">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/anime">动漫库</el-menu-item>
          <el-menu-item index="/news">资讯</el-menu-item>
          <el-menu-item v-if="currentUser" index="/profile">个人中心</el-menu-item>
          <el-submenu v-if="isAdmin" index="/admin">
            <template slot="title">后台管理</template>
            <el-menu-item index="/admin/stats">统计看板</el-menu-item>
            <el-menu-item index="/admin/anime">动漫管理</el-menu-item>
            <el-menu-item index="/admin/category">分类管理</el-menu-item>
            <el-menu-item index="/admin/tag">标签管理</el-menu-item>
            <el-menu-item index="/admin/news">资讯管理</el-menu-item>
            <el-menu-item index="/admin/comment">评论管理</el-menu-item>
            <el-menu-item index="/admin/user">用户管理</el-menu-item>
          </el-submenu>
        </el-menu>
        <div class="auth-actions">
          <template v-if="currentUser">
            <span class="user-name">{{ currentUser.nickname || currentUser.username }}</span>
            <el-button size="mini" plain @click="handleLogout">退出</el-button>
          </template>
          <template v-else>
            <el-button size="mini" plain @click="$router.push('/login')">登录</el-button>
            <el-button size="mini" type="primary" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script>
import { getUser, removeAuth } from './utils/auth'

export default {
  name: 'App',
  data() {
    return { currentUser: null }
  },
  computed: {
    isAdmin() {
      return this.currentUser && this.currentUser.roles && this.currentUser.roles.includes('ADMIN')
    }
  },
  created() {
    this.refreshUser()
    window.addEventListener('auth-change', this.refreshUser)
  },
  beforeDestroy() {
    window.removeEventListener('auth-change', this.refreshUser)
  },
  methods: {
    refreshUser() {
      this.currentUser = getUser()
    },
    handleLogout() {
      removeAuth()
      this.$message.success('已退出登录')
      if (this.$route.meta.requiresAuth) {
        this.$router.push('/home')
      }
    }
  }
}
</script>
