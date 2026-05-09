<template>
  <section v-loading="loading">
    <el-button class="back-button" icon="el-icon-arrow-left" @click="$router.push('/news')">返回资讯</el-button>
    <article v-if="news" class="news-detail">
      <img class="news-detail-cover" :src="news.coverImage" :alt="news.title" loading="lazy">
      <div class="news-detail-body">
        <span class="eyebrow">News Detail</span>
        <h1>{{ news.title }}</h1>
        <div class="news-meta">
          <span>{{ formatTime(news.publishTime) }}</span>
          <span>浏览 {{ news.viewCount || 0 }}</span>
        </div>
        <p class="news-summary">{{ news.summary }}</p>
        <div class="news-content">{{ news.content }}</div>
      </div>
    </article>
  </section>
</template>

<script>
import { getNewsDetail } from '../api/news'

export default {
  name: 'NewsDetail',
  data() {
    return {
      loading: false,
      news: null
    }
  },
  created() {
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        const result = await getNewsDetail(this.$route.params.id)
        this.news = result.data
      } finally {
        this.loading = false
      }
    },
    formatTime(value) {
      return value ? String(value).replace('T', ' ').slice(0, 16) : ''
    }
  }
}
</script>
