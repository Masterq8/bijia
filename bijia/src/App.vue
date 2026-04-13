<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import {
  Lock,
  OfficeBuilding,
  Iphone,
  Message,
  Postcard,
  User,
  CircleCheck,
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const viewMode = ref('login')
const loginFormRef = ref()
const registerFormRef = ref()
const loginCaptchaRef = ref()
const registerCaptchaRef = ref()

const loginCaptchaCode = ref('')
const registerCaptchaCode = ref('')

const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  rememberMe: false,
})

const registerForm = reactive({
  username: '',
  company: '',
  password: '',
  email: '',
  confirmPassword: '',
  identity: '',
  mobile: '',
  code: '',
  agree: true,
})

const identityOptions = [
  { label: '请选择主要身份表述', value: '' },
  { label: '企业管理员', value: 'admin' },
  { label: '采购人员', value: 'purchaser' },
  { label: '访客', value: 'visitor' },
]

const loginRules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

const registerRules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  company: [{ required: true, message: '请输入工作单位', trigger: 'blur' }],
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
  mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
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

const formCardClass = computed(() =>
  viewMode.value === 'login' ? 'auth-card auth-card-login' : 'auth-card auth-card-register',
)

function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min
}

function makeCaptcha() {
  const left = randomInt(2, 9)
  const right = randomInt(2, 9)
  return {
    text: `${left}*${right}=?`,
    answer: String(left * right),
  }
}

function drawCaptcha(canvas, text) {
  if (!canvas) {
    return
  }
  const ctx = canvas.getContext('2d')
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  ctx.fillStyle = '#f6f7f8'
  ctx.fillRect(0, 0, canvas.width, canvas.height)

  for (let i = 0; i < 4; i += 1) {
    ctx.strokeStyle = `rgba(${randomInt(120, 180)},${randomInt(120, 180)},${randomInt(120, 180)},0.7)`
    ctx.beginPath()
    ctx.moveTo(randomInt(0, canvas.width), randomInt(0, canvas.height))
    ctx.lineTo(randomInt(0, canvas.width), randomInt(0, canvas.height))
    ctx.stroke()
  }

  ctx.font = 'bold 34px Georgia'
  ctx.fillStyle = '#2544cc'
  ctx.setTransform(1, 0, -0.18, 1, 0, 0)
  ctx.fillText(text, 16, 42)
  ctx.setTransform(1, 0, 0, 1, 0, 0)
}

function refreshLoginCaptcha() {
  const captcha = makeCaptcha()
  loginCaptchaCode.value = captcha.answer
  drawCaptcha(loginCaptchaRef.value, captcha.text)
}

function refreshRegisterCaptcha() {
  const captcha = makeCaptcha()
  registerCaptchaCode.value = captcha.answer
  drawCaptcha(registerCaptchaRef.value, captcha.text)
}

function switchTo(mode) {
  viewMode.value = mode
  nextTick(() => {
    if (mode === 'login') {
      refreshLoginCaptcha()
    } else {
      refreshRegisterCaptcha()
    }
  })
}

async function handleLogin() {
  await loginFormRef.value.validate((valid) => {
    if (!valid) {
      return false
    }
    if (loginForm.code.trim() !== loginCaptchaCode.value) {
      ElMessage.error('验证码错误')
      refreshLoginCaptcha()
      loginForm.code = ''
      return false
    }
    ElMessage.success('登录验证通过（演示）')
    return true
  })
}

async function handleRegister() {
  await registerFormRef.value.validate((valid) => {
    if (!valid) {
      return false
    }
    if (registerForm.code.trim() !== registerCaptchaCode.value) {
      ElMessage.error('验证码错误')
      refreshRegisterCaptcha()
      registerForm.code = ''
      return false
    }
    ElMessage.success('注册验证通过（演示）')
    return true
  })
}

onMounted(() => {
  refreshLoginCaptcha()
})
</script>

<template>
  <div class="auth-page">
    <div :class="formCardClass">
      <h2 class="card-title">{{ viewMode === 'login' ? '用户登录' : '注册账号' }}</h2>

      <el-form
        v-if="viewMode === 'login'"
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
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
            <el-input v-model="loginForm.code" placeholder="验证码" :prefix-icon="CircleCheck" size="large" />
          </el-form-item>
          <canvas
            ref="loginCaptchaRef"
            class="captcha-canvas"
            width="120"
            height="48"
            @click="refreshLoginCaptcha"
          />
        </div>

        <div class="remember-wrap">
          <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
        </div>

        <el-button type="primary" class="submit-btn" size="large" @click="handleLogin">登 录</el-button>

        <div class="footer-links footer-links-login">
          <a href="javascript:void(0)" @click="switchTo('register')">立即注册</a>
          <a href="javascript:void(0)">忘记密码?</a>
          <a href="javascript:void(0)">返回首页</a>
        </div>
      </el-form>

      <el-form
        v-else
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="auth-form register-form"
        label-position="top"
      >
        <div class="grid-two">
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" placeholder="账号" :prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="company">
            <el-input
              v-model="registerForm.company"
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

          <el-form-item prop="mobile">
            <el-input v-model="registerForm.mobile" placeholder="请输入手机号" :prefix-icon="Iphone" size="large" />
          </el-form-item>

          <div class="captcha-row captcha-row-register">
            <el-form-item prop="code" class="captcha-input-item">
              <el-input v-model="registerForm.code" placeholder="验证码" :prefix-icon="Postcard" size="large" />
            </el-form-item>
            <canvas
              ref="registerCaptchaRef"
              class="captcha-canvas"
              width="130"
              height="48"
              @click="refreshRegisterCaptcha"
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

        <el-button type="primary" class="submit-btn register-btn" size="large" @click="handleRegister">注 册</el-button>

        <div class="footer-links footer-links-register">
          <a href="javascript:void(0)" @click="switchTo('login')">使用已有账户登录</a>
          <a href="javascript:void(0)">返回首页</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background-image: linear-gradient(rgba(18, 56, 71, 0.18), rgba(18, 56, 71, 0.18)),
    url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1920&q=80');
  background-size: cover;
  background-position: center;
}

.auth-card {
  background: rgba(248, 248, 248, 0.94);
  border-radius: 8px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.16);
  padding: 28px 28px 20px;
}

.auth-card-login {
  width: 440px;
}

.auth-card-register {
  width: 870px;
}

.card-title {
  margin: 0 0 28px;
  text-align: center;
  color: #767e89;
  font-size: 39px;
  font-weight: 500;
}

.auth-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.auth-form :deep(.el-input__wrapper),
.auth-form :deep(.el-select__wrapper) {
  height: 48px;
  border-radius: 3px;
  box-shadow: 0 0 0 1px #d8dce5 inset;
  background: #fff;
}

.auth-form :deep(.el-input__prefix-inner) {
  color: #b6bdc7;
}

.captcha-row {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}

.captcha-row-login .captcha-input-item {
  width: 68%;
  margin-bottom: 0;
}

.captcha-row-register {
  width: 100%;
}

.captcha-row-register .captcha-input-item {
  flex: 1;
  margin-bottom: 0;
}

.captcha-canvas {
  width: 120px;
  height: 48px;
  border: 1px solid #9ec39e;
  cursor: pointer;
  border-radius: 2px;
  background: #f4f5f6;
}

.remember-wrap {
  margin: 20px 0 14px;
  color: #9aa3b1;
}

.submit-btn {
  width: 100%;
  height: 44px;
  border-radius: 4px;
  font-size: 28px;
  letter-spacing: 6px;
  font-weight: 500;
}

.footer-links {
  margin-top: 18px;
  display: flex;
  justify-content: space-between;
  color: #409eff;
  font-size: 32px;
}

.footer-links a {
  text-decoration: none;
  color: #409eff;
}

.register-form {
  margin-top: 8px;
}

.grid-two {
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 30px;
}

.agree-item {
  margin: 14px 0 26px;
  display: flex;
  justify-content: center;
}

.agree-item :deep(.el-form-item__content) {
  justify-content: center;
}

.agree-item a {
  text-decoration: none;
  color: #409eff;
}

.register-btn {
  width: 52%;
  display: block;
  margin: 0 auto;
}

.footer-links-register {
  margin-top: 12px;
  padding: 0 8px;
}

@media (max-width: 920px) {
  .auth-page {
    padding: 14px;
  }

  .auth-card-register,
  .auth-card-login {
    width: 100%;
    max-width: 560px;
  }

  .card-title {
    font-size: 28px;
    margin-bottom: 20px;
  }

  .grid-two {
    grid-template-columns: 1fr;
    column-gap: 0;
  }

  .captcha-row-login .captcha-input-item {
    width: calc(100% - 134px);
  }

  .submit-btn {
    font-size: 22px;
    letter-spacing: 2px;
  }

  .register-btn {
    width: 100%;
  }

  .footer-links {
    font-size: 20px;
    gap: 12px;
    flex-wrap: wrap;
    justify-content: center;
  }

  .footer-links-register {
    justify-content: space-between;
  }
}
</style>
