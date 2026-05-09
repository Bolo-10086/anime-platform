<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>评论管理</h1>
        <p>管理用户评论的显示状态，维护前台互动内容质量。</p>
      </div>
    </div>

    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <el-form :inline="true" @submit.native.prevent>
          <el-form-item><el-input v-model.trim="query.keyword" clearable placeholder="动漫 / 用户 / 内容" @keyup.enter.native="loadList" @clear="loadList" /></el-form-item>
          <el-form-item>
            <el-select v-model="query.status" clearable placeholder="评论状态" @change="loadList">
              <el-option label="显示" :value="1" />
              <el-option label="隐藏" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item><el-button icon="el-icon-search" @click="loadList">查询</el-button></el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="commentList" stripe border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="animeTitle" label="动漫" min-width="160" />
        <el-table-column label="用户" width="140">
          <template slot-scope="{ row }">{{ row.nickname || row.username }}</template>
        </el-table-column>
        <el-table-column prop="content" label="评论内容" min-width="280" />
        <el-table-column label="状态" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '显示' : '隐藏' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时间" width="160">
          <template slot-scope="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="toggleStatus(row)">
              {{ row.status === 1 ? '隐藏' : '显示' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </section>
</template>

<script>
import { getAdminCommentList, updateCommentStatus } from '../api/admin'

export default {
  name: 'AdminComment',
  data() {
    return {
      loading: false,
      commentList: [],
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
        const result = await getAdminCommentList(this.query)
        this.commentList = result.data || []
      } finally {
        this.loading = false
      }
    },
    async toggleStatus(row) {
      const nextStatus = row.status === 1 ? 0 : 1
      const label = nextStatus === 1 ? '显示' : '隐藏'
      try {
        await this.$confirm(`确认${label}这条评论吗？`, '状态确认', { type: 'warning' })
        await updateCommentStatus(row.id, nextStatus)
        this.$message.success('评论状态已更新')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('状态更新失败')
      }
    },
    formatTime(value) {
      return value ? String(value).replace('T', ' ').slice(0, 16) : ''
    }
  }
}
</script>
