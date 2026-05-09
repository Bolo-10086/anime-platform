<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>用户管理</h1>
      </div>
    </div>

    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <el-form :inline="true" @submit.native.prevent>
          <el-form-item><el-input v-model.trim="query.keyword" clearable placeholder="用户名 / 昵称 / 邮箱" @keyup.enter.native="loadList" @clear="loadList" /></el-form-item>
          <el-form-item>
            <el-select v-model="query.status" clearable placeholder="账号状态" @change="loadList">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item><el-button icon="el-icon-search" @click="loadList">查询</el-button></el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="userList" stripe border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="130" />
        <el-table-column prop="nickname" label="昵称" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column label="角色" width="140">
          <template slot-scope="{ row }">
            <el-tag v-for="role in row.roles" :key="role" size="mini" :type="role === 'ADMIN' ? 'warning' : 'success'">{{ role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80" />
        <el-table-column prop="favoriteCount" label="收藏" width="80" />
        <el-table-column prop="ratingCount" label="评分" width="80" />
        <el-table-column label="操作" width="130" fixed="right">
          <template slot-scope="{ row }">
            <el-button v-if="row.id !== 1" size="mini" type="text" @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </section>
</template>

<script>
import { getAdminUserList, updateUserStatus } from '../api/admin'

export default {
  name: 'AdminUser',
  data() {
    return {
      loading: false,
      userList: [],
      query: { keyword: '', status: '' }
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const result = await getAdminUserList(this.query)
        this.userList = result.data || []
      } finally {
        this.loading = false
      }
    },
    async toggleStatus(row) {
      const nextStatus = row.status === 1 ? 0 : 1
      const label = nextStatus === 1 ? '启用' : '禁用'
      try {
        await this.$confirm(`确认${label}用户“${row.username}”吗？`, '状态确认', { type: 'warning' })
        await updateUserStatus(row.id, nextStatus)
        this.$message.success('状态已更新')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('状态更新失败')
      }
    }
  }
}
</script>
