<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Dashboard</span>
        <h1>后台统计看板</h1>
      </div>
      <el-button type="primary" icon="el-icon-refresh" :loading="loading" @click="loadStats">刷新</el-button>
    </div>

    <el-row :gutter="18" class="stat-row">
      <el-col v-for="item in statCards" :key="item.label" :xs="24" :sm="12" :lg="4">
        <el-card shadow="never" class="stat-card"><span class="stat-number">{{ item.value }}</span><strong>{{ item.label }}</strong></el-card>
      </el-col>
    </el-row>

    <el-row :gutter="18">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">分类占比</div><div ref="categoryChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">年份分布</div><div ref="yearChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">评分分布</div><div ref="scoreChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">浏览排行</div><div ref="viewChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">收藏排行</div><div ref="favoriteChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">资讯浏览排行</div><div ref="newsChart" class="chart"></div></el-card>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import * as echarts from 'echarts'
import { getAdminStats } from '../api/stats'

export default {
  name: 'AdminStats',
  data() {
    return {
      loading: false,
      stats: null,
      charts: {}
    }
  },
  computed: {
    statCards() {
      const stats = this.stats || {}
      return [
        { label: '动漫数量', value: stats.animeCount || 0 },
        { label: '用户数量', value: stats.userCount || 0 },
        { label: '评论数量', value: stats.commentCount || 0 },
        { label: '收藏数量', value: stats.favoriteCount || 0 },
        { label: '评分数量', value: stats.ratingCount || 0 }
      ]
    }
  },
  mounted() {
    this.loadStats()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    Object.values(this.charts).forEach(chart => chart && chart.dispose())
  },
  methods: {
    async loadStats() {
      this.loading = true
      try {
        const result = await getAdminStats()
        this.stats = result.data
        this.$nextTick(this.renderCharts)
      } finally {
        this.loading = false
      }
    },
    renderCharts() {
      if (!this.stats) return
      const categoryChart = this.getChart('categoryChart')
      const yearChart = this.getChart('yearChart')
      const scoreChart = this.getChart('scoreChart')
      const viewChart = this.getChart('viewChart')
      const favoriteChart = this.getChart('favoriteChart')
      const newsChart = this.getChart('newsChart')
      categoryChart.setOption({
        color: ['#e11d48', '#2563eb', '#f59e0b', '#10b981', '#8b5cf6', '#14b8a6'],
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['42%', '70%'], data: this.stats.categoryStats || [], label: { formatter: '{b}: {c}' } }]
      })
      const yearStats = this.stats.yearStats || []
      yearChart.setOption({
        color: ['#2563eb'],
        tooltip: { trigger: 'axis' },
        grid: { left: 42, right: 20, top: 24, bottom: 42 },
        xAxis: { type: 'category', data: yearStats.map(item => item.name) },
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: yearStats.map(item => item.value), barMaxWidth: 34 }]
      })
      const scoreStats = this.stats.scoreStats || []
      scoreChart.setOption({
        color: ['#f59e0b'],
        tooltip: { trigger: 'axis' },
        grid: { left: 42, right: 20, top: 24, bottom: 42 },
        xAxis: { type: 'category', data: scoreStats.map(item => item.name) },
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: scoreStats.map(item => item.value), barMaxWidth: 42 }]
      })
      this.renderHorizontalBar(viewChart, this.stats.topViewedAnime || [], '#14b8a6')
      this.renderHorizontalBar(favoriteChart, this.stats.topFavoriteAnime || [], '#e11d48')
      this.renderHorizontalBar(newsChart, this.stats.topViewedNews || [], '#8b5cf6')
    },
    getChart(refName) {
      if (!this.charts[refName]) this.$set(this.charts, refName, echarts.init(this.$refs[refName]))
      return this.charts[refName]
    },
    renderHorizontalBar(chart, rows, color) {
      chart.setOption({
        color: [color],
        tooltip: { trigger: 'axis' },
        grid: { left: 90, right: 24, top: 18, bottom: 28 },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: rows.map(item => item.name).reverse(), axisLabel: { width: 78, overflow: 'truncate' } },
        series: [{ type: 'bar', data: rows.map(item => item.value).reverse(), barMaxWidth: 18 }]
      })
    },
    resizeCharts() {
      Object.values(this.charts).forEach(chart => chart && chart.resize())
    }
  }
}
</script>
