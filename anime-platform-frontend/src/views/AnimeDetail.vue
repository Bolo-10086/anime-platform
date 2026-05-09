<template>
  <section v-loading="loading" class="detail-page">
    <el-button class="back-button" icon="el-icon-arrow-left" @click="$router.push('/anime')">返回动漫库</el-button>
    <div v-if="anime" class="detail-hero" :style="{ backgroundImage: `linear-gradient(90deg, rgba(17,24,39,.94), rgba(17,24,39,.78), rgba(17,24,39,.38)), url(${anime.coverImage})` }">
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
          <el-button class="wide-button" :type="state.favorite ? 'danger' : 'primary'" icon="el-icon-star-on" @click="toggleFavorite">
            {{ state.favorite ? '取消收藏' : '收藏动漫' }}
          </el-button>
          <div class="rating-box">
            <span>我的评分</span>
            <el-rate v-model="ratingValue" :max="10" show-score @change="submitRating" />
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="interaction-card">
          <div slot="header" class="card-header">
            <span>评论区</span>
            <el-button size="small" @click="loadComments">刷新</el-button>
          </div>
          <el-input v-model="commentContent" type="textarea" :rows="3" maxlength="500" show-word-limit placeholder="写下你的看法" />
          <div class="comment-submit">
            <el-button type="primary" :loading="commenting" @click="submitComment">发表评论</el-button>
          </div>
          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-meta">
                <strong>{{ comment.nickname || comment.username || '用户' }}</strong>
                <span>{{ formatTime(comment.createTime) }}</span>
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
  favoriteAnime,
  getAnimeComments,
  getAnimeDetail,
  getUserAnimeState,
  rateAnime
} from '../api/anime'
import { hasToken } from '../utils/auth'

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
      ratingValue: 0
    }
  },
  created() {
    this.loadPage()
  },
  methods: {
    async loadPage() {
      this.loading = true
      try {
        await Promise.all([this.loadDetail(), this.loadComments()])
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
    async loadState() {
      const result = await getUserAnimeState(this.$route.params.id)
      this.state = result.data || { favorite: false, rating: null }
      this.ratingValue = Number(this.state.rating || 0)
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
      await this.loadDetail()
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
        await this.loadComments()
        this.$message.success('评论已发布')
      } finally {
        this.commenting = false
      }
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
