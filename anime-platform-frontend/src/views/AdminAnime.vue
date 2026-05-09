<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>动漫管理</h1>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="openCreate">新增动漫</el-button>
    </div>

    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <el-form :inline="true" @submit.native.prevent>
          <el-form-item>
            <el-input v-model.trim="query.keyword" clearable placeholder="搜索动漫" @keyup.enter.native="loadList" @clear="loadList" />
          </el-form-item>
          <el-form-item>
            <el-select v-model="query.categoryId" clearable placeholder="分类" @change="loadList">
              <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="query.status" clearable placeholder="状态" @change="loadList">
              <el-option label="已完结" value="已完结" />
              <el-option label="连载中" value="连载中" />
              <el-option label="未开播" value="未开播" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" @click="loadList">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="animeList" stripe border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="封面" width="86">
          <template slot-scope="{ row }"><img class="cover" :src="row.coverImage" :alt="row.title"></template>
        </el-table-column>
        <el-table-column prop="title" label="名称" min-width="170" />
        <el-table-column prop="categoryName" label="分类" width="90" />
        <el-table-column prop="tagNames" label="标签" min-width="180" />
        <el-table-column prop="releaseYear" label="年份" width="80" />
        <el-table-column prop="type" label="类型" width="90" />
        <el-table-column prop="score" label="评分" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="openEdit(row)">编辑</el-button>
            <el-button size="mini" type="text" class="danger-text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="820px">
      <el-form ref="form" :model="form" :rules="rules" label-width="98px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="动漫名称" prop="title"><el-input v-model.trim="form.title" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="原名"><el-input v-model.trim="form.originalTitle" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="分类" prop="categoryName">
              <el-select v-model="form.categoryName" @change="syncCategory">
                <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.name" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8"><el-form-item label="年份"><el-input-number v-model="form.releaseYear" :min="2010" :max="2030" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="评分"><el-input-number v-model="form.score" :min="0" :max="10" :step="0.1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="状态">
              <el-select v-model="form.status">
                <el-option label="已完结" value="已完结" />
                <el-option label="连载中" value="连载中" />
                <el-option label="未开播" value="未开播" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8"><el-form-item label="集数"><el-input-number v-model="form.episodes" :min="0" /></el-form-item></el-col>
          <el-col :span="8">
            <el-form-item label="类型">
              <el-select v-model="form.type">
                <el-option label="TV" value="TV" />
                <el-option label="剧场版" value="剧场版" />
                <el-option label="ONA" value="ONA" />
                <el-option label="OVA" value="OVA" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="地区"><el-input v-model.trim="form.region" /></el-form-item></el-col>
          <el-col :span="16">
            <el-form-item label="标签">
              <el-select v-model="selectedTags" multiple filterable placeholder="选择标签" @change="syncTags">
                <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.name" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面链接" prop="coverImage"><el-input v-model.trim="form.coverImage" /></el-form-item>
        <el-form-item label="横幅链接"><el-input v-model.trim="form.bannerImage" /></el-form-item>
        <el-form-item label="正版来源"><el-input v-model.trim="form.sourceName" /></el-form-item>
        <el-form-item label="观看入口"><el-input v-model.trim="form.watchUrl" /></el-form-item>
        <el-form-item label="预告入口"><el-input v-model.trim="form.trailerUrl" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import { createAnime, deleteAnime, getAdminAnimeList, updateAnime } from '../api/anime'
import { getAdminCategoryList, getAdminTagList } from '../api/category'

const emptyForm = () => ({
  id: null,
  title: '',
  originalTitle: '',
  coverImage: '',
  bannerImage: '',
  categoryId: null,
  categoryName: '',
  tagNames: '',
  region: '日本',
  type: 'TV',
  releaseYear: 2024,
  status: '已完结',
  episodes: 12,
  score: 8,
  description: '',
  sourceName: '',
  watchUrl: '',
  trailerUrl: '',
  viewCount: 0
})

export default {
  name: 'AdminAnime',
  data() {
    return {
      loading: false,
      saving: false,
      dialogVisible: false,
      animeList: [],
      categories: [],
      tags: [],
      selectedTags: [],
      query: { keyword: '', categoryId: '', status: '' },
      form: emptyForm(),
      rules: {
        title: [{ required: true, message: '请输入动漫名称', trigger: 'blur' }],
        categoryName: [{ required: true, message: '请选择分类', trigger: 'change' }],
        coverImage: [{ required: true, message: '请输入封面链接', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.form.id ? '编辑动漫' : '新增动漫'
    }
  },
  created() {
    this.loadOptions()
    this.loadList()
  },
  methods: {
    async loadOptions() {
      const [categoryResult, tagResult] = await Promise.all([getAdminCategoryList(), getAdminTagList()])
      this.categories = categoryResult.data || []
      this.tags = tagResult.data || []
    },
    async loadList() {
      this.loading = true
      try {
        const result = await getAdminAnimeList(this.query)
        this.animeList = result.data || []
      } finally {
        this.loading = false
      }
    },
    openCreate() {
      this.form = emptyForm()
      this.selectedTags = []
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...emptyForm(), ...row }
      this.selectedTags = row.tagNames ? row.tagNames.split(',').map(item => item.trim()).filter(Boolean) : []
      this.dialogVisible = true
    },
    syncCategory(name) {
      const category = this.categories.find(item => item.name === name)
      this.form.categoryId = category ? category.id : null
      this.form.categoryName = name
    },
    syncTags(values) {
      this.form.tagNames = values.join(',')
    },
    handleSave() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          this.syncTags(this.selectedTags)
          if (this.form.id) await updateAnime(this.form.id, this.form)
          else await createAnime(this.form)
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadList()
        } catch (error) {
          const message = error.response && error.response.data ? error.response.data.message : '保存失败'
          this.$message.error(message)
        } finally {
          this.saving = false
        }
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除《${row.title}》吗？`, '删除确认', { type: 'warning' })
        await deleteAnime(row.id)
        this.$message.success('删除成功')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('删除失败')
      }
    }
  }
}
</script>
