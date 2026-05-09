<template>
  <section>
    <div class="page-head">
      <div>
        <span class="eyebrow">Admin</span>
        <h1>分类管理</h1>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="openCreate">新增分类</el-button>
    </div>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="categoryList" stripe border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" width="150" />
        <el-table-column prop="description" label="分类描述" min-width="260" />
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
import { createCategory, deleteCategory, getAdminCategoryList, updateCategory } from '../api/category'

const emptyForm = () => ({ id: null, name: '', description: '', sortOrder: 0 })

export default {
  name: 'AdminCategory',
  data() {
    return {
      loading: false,
      saving: false,
      dialogVisible: false,
      categoryList: [],
      form: emptyForm(),
      rules: { name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }] }
    }
  },
  computed: {
    dialogTitle() {
      return this.form.id ? '编辑分类' : '新增分类'
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const result = await getAdminCategoryList()
        this.categoryList = result.data || []
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
          if (this.form.id) await updateCategory(this.form.id, this.form)
          else await createCategory(this.form)
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
        await this.$confirm(`确认删除分类“${row.name}”吗？`, '删除确认', { type: 'warning' })
        await deleteCategory(row.id)
        this.$message.success('删除成功')
        this.loadList()
      } catch (error) {
        if (error !== 'cancel') this.$message.error('删除失败')
      }
    }
  }
}
</script>
