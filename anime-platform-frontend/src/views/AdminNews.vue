<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>资讯管理</h1>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="openCreate">新增资讯</el-button>
    </div>

    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <el-form :inline="true" @submit.native.prevent>
          <el-form-item><el-input v-model.trim="query.keyword" clearable placeholder="搜索资讯" @keyup.enter.native="loadList" @clear="loadList" /></el-form-item>
          <el-form-item>
            <el-select v-model="query.status" clearable placeholder="状态" @change="loadList">
              <el-option label="发布" :value="1" />
              <el-option label="草稿" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item><el-button icon="el-icon-search" @click="loadList">查询</el-button></el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="newsList" stripe border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="封面" width="92">
          <template slot-scope="{ row }"><img class="cover" :src="row.coverImage" :alt="row.title"></template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="220" />
        <el-table-column prop="summary" label="摘要" min-width="260" />
        <el-table-column label="状态" width="90">
          <template slot-scope="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '发布' : '草稿' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="90" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" class="danger-text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="760px">
      <el-form ref="form" :model="form" :rules="rules" label-width="86px">
        <el-form-item label="标题" prop="title"><el-input v-model.trim="form.title" /></el-form-item>
        <el-form-item label="封面"><el-input v-model.trim="form.coverImage" /></el-form-item>
        <el-form-item label="摘要" prop="summary"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="8" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio-button :label="1">发布</el-radio-button>
            <el-radio-button :label="0">草稿</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import { createNews, deleteNews, getAdminNewsList, updateNews } from '../api/news'

const emptyForm = () => ({
  id: null,
  title: '',
  coverImage: 'https://myanimelist.net/images/anime/1015/138006l.jpg',
  summary: '',
  content: '',
  status: 1,
  viewCount: 0
})

export default {
  name: 'AdminNews',
  data() {
    return {
      loading: false,
      saving: false,
      dialogVisible: false,
      newsList: [],
      query: { keyword: '', status: '' },
      form: emptyForm(),
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        summary: [{ required: true, message: '请输入摘要', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.form.id ? '编辑资讯' : '新增资讯'
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const params = { keyword: this.query.keyword }
        if (this.query.status !== '') params.status = this.query.status
        const result = await getAdminNewsList(params)
        this.newsList = result.data || []
      } finally {
        this.loading = false
      }
    },
    openCreate() {
      this.form = emptyForm()
      this.dialogVisible = true
    },
    openEdit(row) {
      this.form = { ...emptyForm(), ...row }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          if (this.form.id) await updateNews(this.form.id, this.form)
          else await createNews(this.form)
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
        await this.$confirm(`确认删除资讯“${row.title}”吗？`, '删除确认', { type: 'warning' })
        await deleteNews(row.id)
        this.$message.success('删除成功')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('删除失败')
      }
    }
  }
}
</script>
