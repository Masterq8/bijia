<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Lock, OfficeBuilding, Iphone, Message, Postcard, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getCodeImg, register } from '@/api/login'

const router = useRouter()
const loading = ref(false)
const registerFormRef = ref()
const captchaInfo = reactive({
  img: '',
  uuid: '',
})

const registerForm = reactive({
  username: '',
  unit: '',
  password: '',
  email: '',
  confirmPassword: '',
  identity: '',
  phonenumber: '',
  code: '',
  uuid: '',
  agree: true,
})

const identityOptions = [
  { label: '请选择主要身份表述', value: '' },
  { label: '企业管理员', value: 'admin' },
  { label: '采购人员', value: 'purchaser' },
  { label: '访客', value: 'visitor' },
]

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  unit: [{ required: true, message: '请输入工作单位', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: ['change', 'blur'] },
  ],
  identity: [{ required: true, message: '请选择身份', trigger: 'change' }],
  phonenumber: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  agree: [
    {
      validator: (_, value, callback) => {
        if (!value) {
          callback(new Error('请先阅读并同意协议'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
}

async function refreshCaptcha() {
  try {
    const response = await getCodeImg()
    if (response.code === 200) {
      captchaInfo.img = response.img?.startsWith('data:image')
        ? response.img
        : `data:image/gif;base64,${response.img}`
      captchaInfo.uuid = response.uuid
      registerForm.uuid = response.uuid
    }
  } catch {
    ElMessage.error('获取验证码失败')
  }
}

async function handleRegister() {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return false

    loading.value = true
    try {
      const response = await register({
        username: registerForm.username,
        password: registerForm.password,
        email: registerForm.email,
        phonenumber: registerForm.phonenumber,
        identity: registerForm.identity,
        unit: registerForm.unit,
        code: registerForm.code,
        uuid: registerForm.uuid,
      })

      if (response.code === 200) {
        ElMessage.success('注册成功，请返回登录')
        router.push('/login')
      }
    } catch (error) {
      registerForm.code = ''
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
    <div class="auth-card auth-card-register">
      <h2 class="card-title">注册账号</h2>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        class="auth-form register-form"
        label-position="top"
      >
        <div class="grid-two">
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" placeholder="账号" :prefix-icon="User" size="large" />
          </el-form-item>

          <el-form-item prop="unit">
            <el-input
              v-model="registerForm.unit"
              placeholder="请输入工作单位"
              :prefix-icon="OfficeBuilding"
              size="large"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              placeholder="密码"
              :prefix-icon="Lock"
              show-password
              size="large"
            />
          </el-form-item>

          <el-form-item prop="email">
            <el-input v-model="registerForm.email" placeholder="请输入邮箱" :prefix-icon="Message" size="large" />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              placeholder="确认密码"
              :prefix-icon="Lock"
              show-password
              size="large"
            />
          </el-form-item>

          <el-form-item prop="identity">
            <el-select v-model="registerForm.identity" placeholder="请选择主要身份表述" size="large">
              <el-option v-for="item in identityOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item prop="phonenumber">
            <el-input
              v-model="registerForm.phonenumber"
              placeholder="请输入手机号"
              :prefix-icon="Iphone"
              size="large"
            />
          </el-form-item>

          <div class="captcha-row captcha-row-register">
            <el-form-item prop="code" class="captcha-input-item">
              <el-input v-model="registerForm.code" placeholder="验证码" :prefix-icon="Postcard" size="large" />
            </el-form-item>
            <img
              :src="captchaInfo.img"
              class="captcha-image"
              alt="验证码"
              title="点击切换验证码"
              @click="refreshCaptcha"
            />
          </div>
        </div>

        <el-form-item prop="agree" class="agree-item">
          <el-checkbox v-model="registerForm.agree">
            同意
            <a href="javascript:void(0)">《用户协议》</a>
            和
            <a href="javascript:void(0)">《隐私政策》</a>
          </el-checkbox>
        </el-form-item>

        <el-button type="primary" class="submit-btn register-btn" size="large" :loading="loading" @click="handleRegister">
          注 册
        </el-button>

        <div class="footer-links footer-links-register">
          <router-link to="/login">使用已有账户登录</router-link>
          <a href="javascript:void(0)">返回首页</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/styles/auth.css';
</style>
