<template>
  <div class="register">

    <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">注册账号</h3>
      <div class="form-split">
    <!-- 左侧列 -->
    <div class="left-column">
  <el-form-item prop="username">
        <el-input
          v-model="registerForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>





      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleRegister"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="确认密码"
          @keyup.enter="handleRegister"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="phonenumber">
      <el-input
        v-model="registerForm.phonenumber"
        placeholder="请输入手机号码"
      ></el-input>
    </el-form-item>
    </div>
    <div class="right-column">
    <el-form-item prop="unit">
      <el-input
        v-model="registerForm.unit"
        placeholder="请输入工作单位"
      ></el-input>
    </el-form-item>
     <el-form-item prop="email">
      <el-input
        v-model="registerForm.email"
        placeholder="请输入邮箱"
      ></el-input>
    </el-form-item>


      <el-form-item  prop="identity">
              <el-select v-model="registerForm.identity" placeholder="请选择主要身份表述">
                <el-option
                    v-for="dict in sys_user_identity"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          size="large"
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 60%"
          @keyup.enter="Register"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>
    </div>
      </div>
      <div class="agreement-container">
        <el-checkbox v-model="agree">
          同意
          <a href="javascript:void(0)" style="color: #409EFF">《用户协议》</a>
          和
          <a href="javascript:void(0)" style="color: #409EFF">《隐私政策》</a>
        </el-checkbox>
      </div>


      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          class="register-btn"
          @click.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div class="link-container">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
          <router-link class="link-type" :to="'/index'">返回首页</router-link>
        </div>
      </el-form-item>



    </el-form>
     <!-- 底部      -->
    <div class="el-register-footer">
      <span></span>
    </div>
  </div>
</template>

<script setup>
import {ElMessage, ElMessageBox} from "element-plus";
import { getCodeImg, register } from "@/api/login";
const router = useRouter();
const { proxy } = getCurrentInstance();

const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  code: "",
  uuid: "",
  identity:"",
  unit:"",
});
 const { sys_user_identity } = proxy.useDict("sys_user_identity");
const equalToPassword = (rule, value, callback) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};



const tp = () =>{
  router.push("/home");
}

const registerRules = {
  username: [
    { required: true, trigger: "blur", message: "请输入您的账号" },
    { min: 2, max: 20, message: "用户账号长度必须介于 2 和 20 之间", trigger: "blur" }
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" },
    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, trigger: "blur", message: "请再次输入您的密码" },
    { required: true, validator: equalToPassword, trigger: "blur" }
  ],
  identity: [
    { required: true, trigger: "change", message: "请选择您的身份" }
  ],
  unit: [
    { required: true, trigger: "change", message: "请输入您的单位" }
  ],
  phonenumber: [
    { required: true, pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  email: [
    { required: true, pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/, message: "请输入正确的邮箱地址", trigger: "blur" }
  ],

  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

const codeUrl = ref("");
const loading = ref(false);
const captchaEnabled = ref(true);
const agree = ref(true);

function handleRegister() {
  if (agree.value){
    Register();
  }else {
      ElMessage.error('请先勾选同意《用户协议》和《隐私政策》')
  }
}


function Register() {
  proxy.$refs.registerRef.validate(valid => {
    if (valid) {
      loading.value = true;
      register(registerForm.value).then(res => {
        const username = registerForm.value.username;
        ElMessageBox.alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", "系统提示", {
          dangerouslyUseHTMLString: true,
          type: "success",
        }).then(() => {
          router.push("/login");
        }).catch(() => {});
      }).catch(() => {
        loading.value = false;
        if (captchaEnabled) {
          getCode();
        }
      });
    }
  });
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      registerForm.value.uuid = res.uuid;
    }
  });
}

getCode();
</script>

<style lang='scss' scoped>
.form-split {
  display: flex;
  justify-content: space-between;
}

.left-column,.right-column {
  flex: 1;
  padding: 0 15px;
}

/* 为了解决某些元素宽度问题，可以添加以下样式 */
.right-column .el-form-item__content {
  flex-basis: 80%;
}
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: var(--text-primary, #707070);
}

.register-form {
  border-radius: 12px;
  background: var(--card-bg, #ffffff);
  width: 90%;
  max-width: 800px;
  padding: 25px 25px 15px 25px;
  box-shadow: var(--shadow-md, 0 8px 32px rgba(0, 0, 0, 0.15));
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 35%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.register-code-img {
  height: 40px;
  padding-left: 12px;
}
/* 新增：链接容器样式 */
.link-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid var(--border-light, #f0f0f0);
  width: 100%;
}

.link-container .link-type {
  color: #409EFF;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.2s;
}

.link-container .link-type:hover {
  color: #66b1ff;
  text-decoration: underline;
}

/* 新增：注册按钮样式 */
.register-btn {
  width: 50%;
  margin: 0 auto;
  display: block;
  border-radius: 8px;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
}

/* 新增：协议容器样式 */
.agreement-container {
  display: flex;
  justify-content: center;
  color: rgb(153, 153, 153);
  margin: 10px 0;
}

/* 响应式处理 */
@media (max-width: 768px) {
  .form-split {
    flex-direction: column;
  }
  
  .left-column, .right-column {
    padding: 0 5px;
  }
  
  .register-form {
    width: 95%;
    padding: 15px;
  }
  
  .register-btn {
    width: 80%;
  }
  
  .link-container {
    flex-direction: column;
    gap: 10px;
  }
}

</style>
