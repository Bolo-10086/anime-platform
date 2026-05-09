<template>
  <section v-loading="loading" class="detail-page">
    <el-button class="back-button" icon="el-icon-arrow-left" @click="$router.push('/anime')">返回动漫库</el-button>

    <div
      v-if="anime"
      class="detail-hero"
      :style="{ backgroundImage: `linear-gradient(90deg, rgba(17,24,39,.94), rgba(17,24,39,.78), rgba(17,24,39,.38)), url(${anime.coverImage})` }"
    >
      <img class="detail-poster" :src="anime.coverImage" :alt="anime.title">
      <div class="detail-info">
        <div class="hero-meta">
          <span>{{ anime.releaseYear }}</span>
          <span>{{ anime.categoryName }}</span>
          <span>{{ anime.type }}</span>
          <span>{{ anime.status }}</span>
        </div>
        <h1>{{ anime.title }}</h1>
        <p class="original-title">{{ anime.originalTitle }}</p>
        <div class="score-line">
          <strong>{{ anime.score }}</strong>
          <span>{{ anime.episodes }} 集</span>
          <span>{{ Number(anime.viewCount || 0).toLocaleString() }} 浏览</span>
        </div>
        <div class="hero-tags">
          <span v-for="tag in splitTags(anime.tagNames)" :key="tag">{{ tag }}</span>
        </div>
        <p class="detail-desc">{{ anime.description }}</p>
        <div class="source-actions">
          <el-button type="primary" icon="el-icon-video-play" :disabled="!anime.watchUrl" @click="openUrl(anime.watchUrl)">正版观看入口</el-button>
          <el-button icon="el-icon-film" :disabled="!anime.trailerUrl" @click="openUrl(anime.trailerUrl)">官方预告</el-button>
        </div>
      </div>
    </div>

    <el-row v-if="anime" :gutter="18" class="interaction-row">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="interaction-card">
          <div slot="header">我的互动</div>
          <div class="interaction-stats">
            <div>
              <strong>{{ summary.favoriteCount }}</strong>
              <span>收藏</span>
            </div>
            <div>
              <strong>{{ summary.ratingCount }}</strong>
              <span>评分</span>
            </div>
            <div>
              <strong>{{ summary.commentCount }}</strong>
              <span>评论</span>
            </div>
          </div>
          <el-button class="wide-button" :type="state.favorite ? 'danger' : 'primary'" icon="el-icon-star-on" @click="toggleFavorite">
            {{ state.favorite ? '取消收藏' : '收藏动漫' }}
          </el-button>
          <div class="rating-box">
            <span>我的评分</span>
            <el-rate v-model="ratingValue" :max="10" allow-half show-score @change="submitRating" />
          </div>
        </el-card>

        <el-card v-if="relatedAnime.length" shadow="never" class="interaction-card related-card">
          <div slot="header">同类推荐</div>
          <div v-for="item in relatedAnime" :key="item.id" class="related-item" @click="$router.push(`/anime/${item.id}`)">
            <img :src="item.coverImage" :alt="item.title">
            <div>
              <strong>{{ item.title }}</strong>
              <span>{{ item.releaseYear }} · {{ item.score }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="interaction-card">
          <div slot="header" class="card-header">
            <span>评论区</span>
            <el-button size="small" @click="refreshComments">刷新</el-button>
          </div>
          <el-input v-model="commentContent" type="textarea" :rows="3" maxlength="500" show-word-limit placeholder="写下你的看法" />
          <div class="comment-submit">
            <el-button type="primary" :loading="commenting" @click="submitComment">发表评论</el-button>
          </div>
          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-meta">
                <strong>{{ comment.nickname || comment.username || '用户' }}</strong>
                <div class="comment-actions">
                  <span>{{ formatTime(comment.createTime) }}</span>
                  <el-button v-if="isOwnComment(comment)" type="text" size="mini" @click="deleteComment(comment.id)">删除</el-button>
                </div>
              </div>
              <p>{{ comment.content }}</p>
            </div>
            <el-empty v-if="!comments.length" description="暂无评论" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import {
  addAnimeComment,
  cancelFavoriteAnime,
  deleteUserComment,
  favoriteAnime,
  getAnimeComments,
  getAnimeDetail,
  getAnimeInteractionSummary,
  getAnimeList,
  getUserAnimeState,
  rateAnime
} from '../api/anime'
import { getUser, hasToken } from '../utils/auth'

const emptySummary = () => ({
  commentCount: 0,
  favoriteCount: 0,
  ratingCount: 0,
  averageRating: null
})

export default {
  name: 'AnimeDetail',
  data() {
    return {
      loading: false,
      commenting: false,
      anime: null,
      comments: [],
      commentContent: '',
      state: { favorite: false, rating: null },
      ratingValue: 0,
      relatedAnime: [],
      summary: emptySummary(),
      currentUser: getUser()
    }
  },
  watch: {
    '$route.params.id'() {
      this.loadPage()
    }
  },
  created() {
    this.loadPage()
  },
  methods: {
    async loadPage() {
      this.loading = true
      try {
        this.currentUser = getUser()
        await this.loadDetail()
        await Promise.all([this.loadComments(), this.loadSummary(), this.loadRelatedAnime()])
        if (hasToken()) await this.loadState()
      } finally {
        this.loading = false
      }
    },
    async loadDetail() {
      const result = await getAnimeDetail(this.$route.params.id)
      this.anime = result.data
    },
    async loadComments() {
      const result = await getAnimeComments(this.$route.params.id)
      this.comments = result.data || []
    },
    async loadSummary() {
      const result = await getAnimeInteractionSummary(this.$route.params.id)
      this.summary = { ...emptySummary(), ...(result.data || {}) }
    },
    async loadRelatedAnime() {
      if (!this.anime || !this.anime.categoryId) {
        this.relatedAnime = []
        return
      }
      const result = await getAnimeList({ categoryId: this.anime.categoryId })
      this.relatedAnime = (result.data || []).filter(item => item.id !== this.anime.id).slice(0, 4)
    },
    async loadState() {
      const result = await getUserAnimeState(this.$route.params.id)
      this.state = result.data || { favorite: false, rating: null }
      this.ratingValue = Number(this.state.rating || 0)
    },
    async refreshComments() {
      await Promise.all([this.loadComments(), this.loadSummary()])
    },
    ensureLogin() {
      if (!hasToken()) {
        this.$router.push({ path: '/login', query: { redirect: this.$route.fullPath } })
        return false
      }
      return true
    },
    async toggleFavorite() {
      if (!this.ensureLogin()) return
      const result = this.state.favorite
        ? await cancelFavoriteAnime(this.$route.params.id)
        : await favoriteAnime(this.$route.params.id)
      this.state = result.data
      await this.loadSummary()
      this.$message.success(this.state.favorite ? '已收藏' : '已取消收藏')
    },
    async submitRating(value) {
      if (!this.ensureLogin()) {
        this.ratingValue = Number(this.state.rating || 0)
        return
      }
      const result = await rateAnime(this.$route.params.id, value)
      this.state = result.data
      this.ratingValue = Number(this.state.rating || 0)
      await Promise.all([this.loadDetail(), this.loadSummary()])
      this.$message.success('评分已保存')
    },
    async submitComment() {
      if (!this.ensureLogin()) return
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }
      this.commenting = true
      try {
        await addAnimeComment(this.$route.params.id, this.commentContent)
        this.commentContent = ''
        await this.refreshComments()
        this.$message.success('评论已发布')
      } finally {
        this.commenting = false
      }
    },
    async deleteComment(commentId) {
      if (!this.ensureLogin()) return
      await this.$confirm('确定删除这条评论吗？', '删除评论', { type: 'warning' })
      await deleteUserComment(commentId)
      await this.refreshComments()
      this.$message.success('评论已删除')
    },
    isOwnComment(comment) {
      return this.currentUser && Number(comment.userId) === Number(this.currentUser.id)
    },
    splitTags(value) {
      return value ? value.split(',').map(item => item.trim()).filter(Boolean) : []
    },
    formatTime(value) {
      return value ? String(value).replace('T', ' ').slice(0, 16) : ''
    },
    openUrl(url) {
      if (url) window.open(url, '_blank', 'noopener,noreferrer')
    }
  }
}
</script>
