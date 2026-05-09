<template>
  <section v-loading="loading" class="home-page">
    <el-carousel
      v-if="featuredAnime.length"
      class="hero-carousel"
      height="420px"
      indicator-position="outside"
      trigger="click"
    >
      <el-carousel-item v-for="item in featuredAnime" :key="item.id">
        <div
          class="hero-anime"
          :style="{ backgroundImage: `linear-gradient(90deg, rgba(17,24,39,.88), rgba(17,24,39,.52), rgba(17,24,39,.18)), url(${displayBanner(item)})` }"
        >
          <div class="hero-content">
            <div class="hero-meta">
              <span>{{ item.releaseYear }}</span>
              <span>{{ item.categoryName }}</span>
              <span>{{ item.type }}</span>
              <span>评分 {{ item.score }}</span>
            </div>
            <h1>{{ item.title }}</h1>
            <p>{{ item.description }}</p>
            <div class="hero-tags">
              <span v-for="tag in splitTags(item.tagNames)" :key="tag">{{ tag }}</span>
            </div>
            <div class="hero-actions">
              <el-button type="primary" icon="el-icon-view" @click="$router.push(`/anime/${item.id}`)">查看详情</el-button>
              <el-button plain icon="el-icon-video-play" @click="openExternal(item.watchUrl)">正版入口</el-button>
            </div>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="home-section">
      <div class="section-title">
        <div>
          <span class="eyebrow">Trending</span>
          <h2>近十年热门动漫</h2>
        </div>
        <el-button type="text" @click="$router.push('/anime')">进入动漫库</el-button>
      </div>
      <div class="poster-row">
        <div v-for="item in topAnime" :key="item.id" class="poster-card" @click="$router.push(`/anime/${item.id}`)">
          <img :src="item.coverImage" :alt="item.title" loading="lazy">
          <div>
            <strong>{{ item.title }}</strong>
            <span>{{ item.releaseYear }} · {{ item.categoryName }}</span>
          </div>
        </div>
      </div>
    </div>

    <el-row :gutter="18" class="home-section">
      <el-col :xs="24" :lg="16">
        <div class="section-title">
          <div>
            <span class="eyebrow">Discover</span>
            <h2>分类浏览</h2>
          </div>
        </div>
        <div class="category-wall">
          <button v-for="category in categories" :key="category.id" @click="goAnimeByCategory(category)">
            <strong>{{ category.name }}</strong>
            <span>{{ category.description }}</span>
          </button>
        </div>
      </el-col>
      <el-col :xs="24" :lg="8">
        <div class="ranking-panel">
          <div class="section-title compact">
            <div>
              <span class="eyebrow">Ranking</span>
              <h2>高评分榜</h2>
            </div>
          </div>
          <div v-for="(item, index) in rankingAnime" :key="item.id" class="rank-item" @click="$router.push(`/anime/${item.id}`)">
            <span>{{ index + 1 }}</span>
            <img :src="item.coverImage" :alt="item.title" loading="lazy">
            <div>
              <strong>{{ item.title }}</strong>
              <em>{{ item.score }}</em>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="18" class="home-section">
      <el-col :xs="24" :lg="14">
        <div class="section-title">
          <div>
            <span class="eyebrow">Tags</span>
            <h2>热门标签</h2>
          </div>
        </div>
        <div class="tag-cloud">
          <el-tag v-for="tag in tags" :key="tag.id" effect="plain" @click="goAnimeByTag(tag)">
            {{ tag.name }}
          </el-tag>
        </div>
      </el-col>
      <el-col :xs="24" :lg="10">
        <div class="news-panel">
          <div class="section-title compact">
            <div>
              <span class="eyebrow">News</span>
              <h2>最新资讯</h2>
            </div>
            <el-button type="text" @click="$router.push('/news')">更多</el-button>
          </div>
          <div v-for="item in newsList" :key="item.id" class="news-line" @click="$router.push(`/news/${item.id}`)">
            <img :src="item.coverImage" :alt="item.title" loading="lazy">
            <div>
              <strong>{{ item.title }}</strong>
              <span>{{ item.summary }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import { getAnimeList } from '../api/anime'
import { getCategoryList, getTagList } from '../api/category'
import { getNewsList } from '../api/news'

export default {
  name: 'Home',
  data() {
    return {
      loading: false,
      animeList: [],
      categories: [],
      tags: [],
      newsList: []
    }
  },
  computed: {
    featuredAnime() {
      return this.animeList.slice(0, 5)
    },
    topAnime() {
      return this.animeList.slice(0, 12)
    },
    rankingAnime() {
      return [...this.animeList].sort((a, b) => Number(b.score || 0) - Number(a.score || 0)).slice(0, 6)
    }
  },
  created() {
    this.loadHome()
  },
  methods: {
    async loadHome() {
      this.loading = true
      try {
        const [animeResult, categoryResult, tagResult, newsResult] = await Promise.all([
          getAnimeList(),
          getCategoryList(),
          getTagList(),
          getNewsList()
        ])
        this.animeList = animeResult.data || []
        this.categories = categoryResult.data || []
        this.tags = (tagResult.data || []).slice(0, 18)
        this.newsList = (newsResult.data || []).slice(0, 4)
      } finally {
        this.loading = false
      }
    },
    goAnimeByCategory(category) {
      this.$router.push({ path: '/anime', query: { categoryId: String(category.id) } })
    },
    goAnimeByTag(tag) {
      this.$router.push({ path: '/anime', query: { tagId: String(tag.id) } })
    },
    splitTags(value) {
      return value ? value.split(',').map(item => item.trim()).filter(Boolean).slice(0, 5) : []
    },
    displayBanner(item) {
      return item.bannerImage || item.coverImage
    },
    openExternal(url) {
      if (url) {
        window.open(url, '_blank', 'noopener,noreferrer')
      }
    }
  }
}
</script>
