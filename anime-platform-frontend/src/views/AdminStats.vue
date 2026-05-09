<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Dashboard</span>
        <h1>后台统计看板</h1>
        <p>汇总动漫、用户、评论、收藏和评分等核心数据。</p>
      </div>
      <el-button type="primary" icon="el-icon-refresh" :loading="loading" @click="loadStats">刷新</el-button>
    </div>

    <el-row :gutter="18" class="stat-row">
      <el-col v-for="item in statCards" :key="item.label" :xs="24" :sm="12" :lg="4">
        <el-card shadow="never"><span class="stat-number">{{ item.value }}</span><p>{{ item.label }}</p></el-card>
      </el-col>
    </el-row>

    <el-row :gutter="18">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">分类占比</div><div ref="categoryChart" class="chart"></div></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card"><div slot="header">浏览排行</div><div ref="viewChart" class="chart"></div></el-card>
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
      categoryChart: null,
      viewChart: null
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
    if (this.categoryChart) this.categoryChart.dispose()
    if (this.viewChart) this.viewChart.dispose()
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
      if (!this.categoryChart) this.categoryChart = echarts.init(this.$refs.categoryChart)
      if (!this.viewChart) this.viewChart = echarts.init(this.$refs.viewChart)
      this.categoryChart.setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['42%', '70%'], data: this.stats.categoryStats || [], label: { formatter: '{b}: {c}' } }]
      })
      const top = this.stats.topViewedAnime || []
      this.viewChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 36, right: 20, top: 20, bottom: 80 },
        xAxis: { type: 'category', data: top.map(item => item.name), axisLabel: { rotate: 25 } },
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: top.map(item => item.value), itemStyle: { color: '#1d8f8a' } }]
      })
    },
    resizeCharts() {
      if (this.categoryChart) this.categoryChart.resize()
      if (this.viewChart) this.viewChart.resize()
    }
  }
}
</script>
