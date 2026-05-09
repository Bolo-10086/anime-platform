<template>
  <div class="profile-page">
    <div class="page-head">
      <div>
        <span class="eyebrow">User Center</span>
        <h1>个人中心</h1>
        <p>集中查看账号资料和个人互动记录。</p>
      </div>
      <el-button type="primary" icon="el-icon-refresh" :loading="loading" @click="loadAll">刷新</el-button>
    </div>

    <el-card class="profile-card" shadow="never" v-if="user">
      <div class="profile-main">
        <div class="profile-avatar">{{ avatarText }}</div>
        <div>
          <h2>{{ user.nickname || user.username }}</h2>
          <p>{{ user.username }}</p>
          <div class="role-row">
            <el-tag v-for="role in user.roles" :key="role" size="small" type="success">{{ role }}</el-tag>
          </div>
        </div>
      </div>
      <el-divider />
      <el-row :gutter="16">
        <el-col :xs="24" :sm="8"><div class="profile-field"><span>邮箱</span><strong>{{ user.email || '未填写' }}</strong></div></el-col>
        <el-col :xs="24" :sm="8"><div class="profile-field"><span>手机号</span><strong>{{ user.phone || '未填写' }}</strong></div></el-col>
        <el-col :xs="24" :sm="8"><div class="profile-field"><span>账号状态</span><strong>{{ user.status === 1 ? '正常' : '禁用' }}</strong></div></el-col>
      </el-row>
    </el-card>

    <el-card class="profile-tabs-card" shadow="never">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我的收藏" name="favorites">
          <el-empty v-if="!favorites.length" description="暂无收藏记录" />
          <div v-else class="activity-grid">
            <div v-for="item in favorites" :key="item.favoriteId" class="activity-item">
              <img :src="item.coverImage" :alt="item.title" loading="lazy">
              <div>
                <h3>{{ item.title }}</h3>
                <p>{{ item.categoryName }} · 站内评分 {{ item.score || '暂无' }}</p>
                <div class="activity-actions">
                  <el-button size="mini" type="primary" @click="$router.push(`/anime/${item.animeId}`)">查看详情</el-button>
                  <el-button v-if="item.watchUrl" size="mini" @click="openExternal(item.watchUrl)">正版入口</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="我的评分" name="ratings">
          <el-empty v-if="!ratings.length" description="暂无评分记录" />
          <el-table v-else :data="ratings" stripe>
            <el-table-column label="动漫" min-width="220">
              <template slot-scope="{ row }"><div class="mini-anime"><img :src="row.coverImage" :alt="row.title" loading="lazy"><span>{{ row.title }}</span></div></template>
            </el-table-column>
            <el-table-column prop="rating" label="我的评分" width="120" />
            <el-table-column prop="animeScore" label="站内均分" width="120" />
            <el-table-column label="操作" width="120">
              <template slot-scope="{ row }"><el-button size="mini" type="text" @click="$router.push(`/anime/${row.animeId}`)">查看</el-button></template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="我的评论" name="comments">
          <el-empty v-if="!comments.length" description="暂无评论记录" />
          <div v-else class="comment-list">
            <div v-for="item in comments" :key="item.commentId" class="comment-item">
              <div class="comment-meta">
                <strong>{{ item.title }}</strong>
                <div class="comment-actions">
                  <el-button type="text" size="mini" @click="$router.push(`/anime/${item.animeId}`)">查看动漫</el-button>
                  <el-button type="text" size="mini" class="danger-text" @click="deleteComment(item.commentId)">删除</el-button>
                </div>
              </div>
              <p>{{ item.content }}</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getProfile } from '../api/auth'
import { deleteUserComment, getUserComments, getUserFavorites, getUserRatings } from '../api/anime'

export default {
  name: 'Profile',
  data() {
    return {
      activeTab: 'favorites',
      comments: [],
      favorites: [],
      loading: false,
      ratings: [],
      user: null
    }
  },
  computed: {
    avatarText() {
      const name = this.user ? (this.user.nickname || this.user.username || 'U') : 'U'
      return name.slice(0, 1).toUpperCase()
    }
  },
  created() {
    this.loadAll()
  },
  methods: {
    async loadAll() {
      this.loading = true
      try {
        const [profileResult, favoriteResult, ratingResult, commentResult] = await Promise.all([
          getProfile(), getUserFavorites(), getUserRatings(), getUserComments()
        ])
        this.user = profileResult.data
        this.favorites = favoriteResult.data || []
        this.ratings = ratingResult.data || []
        this.comments = commentResult.data || []
      } finally {
        this.loading = false
      }
    },
    openExternal(url) {
      window.open(url, '_blank', 'noopener,noreferrer')
    },
    async deleteComment(commentId) {
      await this.$confirm('确定删除这条评论吗？', '删除评论', { type: 'warning' })
      await deleteUserComment(commentId)
      this.comments = this.comments.filter(item => item.commentId !== commentId)
      this.$message.success('评论已删除')
    }
  }
}
</script>
