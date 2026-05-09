<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="never">
      <div class="auth-head">
        <h1>注册</h1>
        <p>创建账号后可参与动漫收藏、评分和评论互动。</p>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-position="top" @keyup.enter.native="handleSubmit">
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model.trim="form.nickname" placeholder="请输入昵称" prefix-icon="el-icon-postcard" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model.trim="form.email" placeholder="请输入邮箱" prefix-icon="el-icon-message" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="至少6位" prefix-icon="el-icon-lock" show-password />
        </el-form-item>
        <el-button type="primary" class="auth-submit" :loading="loading" @click="handleSubmit">注册并登录</el-button>
      </el-form>
      <div class="auth-foot">
        <span>已有账号？</span>
        <el-button type="text" @click="$router.push('/login')">去登录</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { register } from '../api/auth'
import { saveAuth } from '../utils/auth'

export default {
  name: 'Register',
  data() {
    return {
      loading: false,
      form: { username: '', nickname: '', email: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const result = await register(this.form)
          saveAuth(result.data)
          this.$message.success('注册成功')
          this.$router.push('/home')
        } catch (error) {
          const message = error.response && error.response.data ? error.response.data.message : '注册失败'
          this.$message.error(message)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
