<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Lock, User, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getCodeImg, login } from '@/api/login'
import { setToken } from '@/utils/auth'

const router = useRouter()
const loading = ref(false)
const loginFormRef = ref()
const captchaInfo = reactive({
  img: '',
  uuid: '',
})

const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  uuid: '',
  rememberMe: false,
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

async function refreshCaptcha() {
  try {
    const response = await getCodeImg()
    if (response.code === 200) {
      captchaInfo.img = response.img?.startsWith('data:image')
        ? response.img
        : `data:image/gif;base64,${response.img}`
      captchaInfo.uuid = response.uuid
      loginForm.uuid = response.uuid
    }
  } catch {
    ElMessage.error('获取验证码失败')
  }
}

async function handleLogin() {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return false

    loading.value = true
    try {
      const response = await login({
        username: loginForm.username,
        password: loginForm.password,
        code: loginForm.code,
        uuid: loginForm.uuid,
      })

      if (response.code === 200) {
        setToken(response.token)
        ElMessage.success('登录成功')
        router.push('/index')
      }
    } catch (error) {
      loginForm.code = ''
      refreshCaptcha()
    } finally {
      loading.value = false
    }
  })
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<template>
  <div class="auth-page">
    <div class="auth-card auth-card-login">
      <h2 class="card-title">用户登录</h2>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="auth-form login-form"
        label-position="top"
      >
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="账号" :prefix-icon="User" size="large" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            size="large"
          />
        </el-form-item>

        <div class="captcha-row captcha-row-login">
          <el-form-item prop="code" class="captcha-input-item">
            <el-input
              v-model="loginForm.code"
              placeholder="验证码"
              :prefix-icon="CircleCheck"
              size="large"
            />
          </el-form-item>
          <img
            :src="captchaInfo.img"
            class="captcha-image"
            alt="验证码"
            title="点击切换验证码"
            @click="refreshCaptcha"
          />
        </div>

        <div class="remember-wrap">
          <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
        </div>

        <el-button type="primary" class="submit-btn" size="large" :loading="loading" @click="handleLogin">
          登 录
        </el-button>

        <div class="footer-links footer-links-login">
          <router-link to="/register">立即注册</router-link>
          <a href="javascript:void(0)">忘记密码?</a>
          <a href="javascript:void(0)">返回首页</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/styles/auth.css';
</style>
