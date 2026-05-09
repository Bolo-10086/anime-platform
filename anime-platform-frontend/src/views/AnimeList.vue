<template>
  <section class="anime-library">
    <div class="library-head">
      <div>
        <span class="eyebrow">Anime Library</span>
        <h1>动漫库</h1>
        <p>按分类、标签、年份、地区和作品类型检索近十年热门动漫。</p>
      </div>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" @submit.native.prevent>
        <el-form-item>
          <el-input
            v-model.trim="query.keyword"
            clearable
            placeholder="搜索名称、原名、简介"
            prefix-icon="el-icon-search"
            @keyup.enter.native="loadAnime"
            @clear="loadAnime"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.categoryId" clearable placeholder="主分类" @change="loadAnime">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="String(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.tagId" clearable placeholder="标签" @change="loadAnime">
            <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="String(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.releaseYear" clearable placeholder="年份" @change="loadAnime">
            <el-option v-for="year in years" :key="year" :label="year" :value="String(year)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.status" clearable placeholder="状态" @change="loadAnime">
            <el-option label="已完结" value="已完结" />
            <el-option label="连载中" value="连载中" />
            <el-option label="未开播" value="未开播" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.region" clearable placeholder="地区" @change="loadAnime">
            <el-option label="日本" value="日本" />
            <el-option label="韩国" value="韩国" />
            <el-option label="中国" value="中国" />
            <el-option label="欧美" value="欧美" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="query.type" clearable placeholder="类型" @change="loadAnime">
            <el-option label="TV" value="TV" />
            <el-option label="剧场版" value="剧场版" />
            <el-option label="ONA" value="ONA" />
            <el-option label="OVA" value="OVA" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadAnime">筛选</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="library-summary">
      <strong>{{ animeList.length }}</strong>
      <span>部作品</span>
      <strong>{{ topScore }}</strong>
      <span>最高评分</span>
      <strong>{{ totalViews }}</strong>
      <span>总浏览量</span>
    </div>

    <div v-loading="loading" class="anime-grid">
      <div v-for="item in animeList" :key="item.id" class="anime-card" @click="$router.push(`/anime/${item.id}`)">
        <div class="anime-poster">
          <img :src="item.coverImage" :alt="item.title">
          <span>{{ item.score }}</span>
        </div>
        <div class="anime-card-body">
          <h3>{{ item.title }}</h3>
          <p>{{ item.releaseYear }} · {{ item.categoryName }} · {{ item.type }}</p>
          <div class="anime-tags">
            <em v-for="tag in splitTags(item.tagNames)" :key="tag">{{ tag }}</em>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && !animeList.length" description="暂无符合条件的动漫" />
    </div>
  </section>
</template>

<script>
import { getAnimeList } from '../api/anime'
import { getCategoryList, getTagList } from '../api/category'

const defaultQuery = () => ({
  keyword: '',
  categoryId: '',
  tagId: '',
  releaseYear: '',
  status: '',
  region: '',
  type: ''
})

export default {
  name: 'AnimeList',
  data() {
    return {
      loading: false,
      categories: [],
      tags: [],
      years: Array.from({ length: 10 }, (_, index) => 2025 - index),
      query: defaultQuery(),
      animeList: []
    }
  },
  computed: {
    topScore() {
      if (!this.animeList.length) return '-'
      return Math.max(...this.animeList.map(item => Number(item.score || 0))).toFixed(1)
    },
    totalViews() {
      return this.animeList.reduce((sum, item) => sum + Number(item.viewCount || 0), 0).toLocaleString()
    }
  },
  watch: {
    '$route.query': {
      handler() {
        this.applyRouteQuery()
        this.loadAnime()
      }
    }
  },
  async created() {
    await this.loadOptions()
    this.applyRouteQuery()
    this.loadAnime()
  },
  methods: {
    async loadOptions() {
      const [categoryResult, tagResult] = await Promise.all([getCategoryList(), getTagList()])
      this.categories = categoryResult.data || []
      this.tags = tagResult.data || []
    },
    applyRouteQuery() {
      const routeQuery = Object.keys(this.$route.query).reduce((result, key) => {
        result[key] = String(this.$route.query[key])
        return result
      }, {})
      this.query = { ...defaultQuery(), ...routeQuery }
    },
    async loadAnime() {
      this.loading = true
      try {
        const params = Object.keys(this.query).reduce((result, key) => {
          if (this.query[key] !== '' && this.query[key] !== null && this.query[key] !== undefined) {
            result[key] = this.query[key]
          }
          return result
        }, {})
        const result = await getAnimeList(params)
        this.animeList = result.data || []
      } catch (error) {
        this.$message.error('动漫列表加载失败')
      } finally {
        this.loading = false
      }
    },
    splitTags(value) {
      return value ? value.split(',').map(item => item.trim()).filter(Boolean).slice(0, 3) : []
    }
  }
}
</script>
