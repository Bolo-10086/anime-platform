<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">News</span>
        <h1>资讯中心</h1>
      </div>
      <el-form class="filter-form" :inline="true" @submit.native.prevent>
        <el-form-item>
          <el-input v-model.trim="query.keyword" clearable placeholder="搜索资讯标题或摘要" prefix-icon="el-icon-search" @keyup.enter.native="loadNews" @clear="loadNews" />
        </el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="loadNews">查询</el-button></el-form-item>
      </el-form>
    </div>

    <el-row :gutter="18">
      <el-col v-for="item in newsList" :key="item.id" :xs="24" :sm="12" :lg="8">
        <el-card class="news-card" shadow="never" @click.native="$router.push(`/news/${item.id}`)">
          <img class="news-cover" :src="item.coverImage" :alt="item.title" loading="lazy">
          <div class="news-body">
            <h2>{{ item.title }}</h2>
            <p>{{ item.summary }}</p>
            <div class="news-meta">
              <span>{{ formatTime(item.publishTime) }}</span>
              <span>浏览 {{ item.viewCount || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="!loading && !newsList.length" description="暂无资讯" />
  </section>
</template>

<script>
import { getNewsList } from '../api/news'

export default {
  name: 'NewsList',
  data() {
    return {
      loading: false,
      query: { keyword: '' },
      newsList: []
    }
  },
  created() {
    this.loadNews()
  },
  methods: {
    async loadNews() {
      this.loading = true
      try {
        const result = await getNewsList(this.query)
        this.newsList = result.data || []
      } finally {
        this.loading = false
      }
    },
    formatTime(value) {
      return value ? String(value).replace('T', ' ').slice(0, 10) : ''
    }
  }
}
</script>
