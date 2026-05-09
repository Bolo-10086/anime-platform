<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>标签管理</h1>
        <p>标签用于更细粒度的题材筛选和首页标签入口。</p>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="openCreate">新增标签</el-button>
    </div>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="tagList" stripe border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="标签名称" width="150" />
        <el-table-column prop="description" label="标签描述" min-width="260" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" class="danger-text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="520px">
      <el-form ref="form" :model="form" :rules="rules" label-width="86px">
        <el-form-item label="名称" prop="name"><el-input v-model.trim="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import { createTag, deleteTag, getAdminTagList, updateTag } from '../api/category'

const emptyForm = () => ({ id: null, name: '', description: '', sortOrder: 0 })

export default {
  name: 'AdminTag',
  data() {
    return {
      loading: false,
      saving: false,
      dialogVisible: false,
      tagList: [],
      form: emptyForm(),
      rules: { name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }] }
    }
  },
  computed: {
    dialogTitle() {
      return this.form.id ? '编辑标签' : '新增标签'
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const result = await getAdminTagList()
        this.tagList = result.data || []
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
          if (this.form.id) await updateTag(this.form.id, this.form)
          else await createTag(this.form)
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
        await this.$confirm(`确认删除标签“${row.name}”吗？`, '删除确认', { type: 'warning' })
        await deleteTag(row.id)
        this.$message.success('删除成功')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('删除失败')
      }
    }
  }
}
</script>
