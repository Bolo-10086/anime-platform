<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="never">
      <div class="auth-head">
        <h1>登录</h1>
        <p>登录后可使用收藏、评分、评论和个人中心功能。</p>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-position="top" @keyup.enter.native="handleSubmit">
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password />
        </el-form-item>
        <el-button type="primary" class="auth-submit" :loading="loading" @click="handleSubmit">登录</el-button>
      </el-form>
      <div class="auth-foot">
        <span>还没有账号？</span>
        <el-button type="text" @click="$router.push('/register')">去注册</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { login } from '../api/auth'
import { saveAuth } from '../utils/auth'

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const result = await login(this.form)
          saveAuth(result.data)
          this.$message.success('登录成功')
          this.$router.push(this.$route.query.redirect || '/home')
        } catch (error) {
          const message = error.response && error.response.data ? error.response.data.message : '登录失败'
          this.$message.error(message)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
