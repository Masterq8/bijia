<template>
  <div class="register">

    <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">重置密码</h3>
      <div class="form-split">
    <!-- 左侧列 -->
    <div class="left-column">
        <el-form-item><spsn style="font-size: 15px;font-weight: bold;">身份验证</spsn></el-form-item>
          <el-form-item prop="username">
                <el-input
                  v-model="registerForm.username"
                  type="text"
                  size="large"
                  auto-complete="off"
                  placeholder="请输入账号"
                >
                  <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
                </el-input>
              </el-form-item>
              <el-form-item prop="phonenumber">
              <el-input
                v-model="registerForm.phonenumber"
                type="text"
                size="large"
                auto-complete="off"
                placeholder="请输入手机号"
              >
                  <template #prefix><svg-icon icon-class="phone" class="el-input__icon input-icon" /></template>
              </el-input>
            </el-form-item>

            <el-form-item prop="verifyType">
                <el-radio-group v-model="registerForm.verifyType">
                    <el-radio
                            v-for="dict in verify_type"
                            :key="dict.value"
                            :value="dict.value"
                    >{{ dict.label }}</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item prop="emailCode">
          <el-input
            v-model="registerForm.emailCode"
            placeholder="请输入验证码"
            class="input-with-button"
          >
            <template #append>
              <el-button
                type="primary"
                :disabled="sending"
             :style="sending ? { color: '#fff', backgroundColor: '#f56c6c', borderColor: '#f56c6c' } : {}"
                @click="sendVerificationCode"
                class="button-in-input"
              >
                {{ sending ? `${countdown}秒后重新获取` : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
    </div>
    <el-divider direction="vertical" style="height: 270px;width: 2px;background-color: #409eff;margin: 0 auto;"></el-divider>
    <div class="right-column">
        <el-form-item style="font-size: 22px;"><spsn style="font-size: 15px;font-weight: bold;">密码重置</spsn></el-form-item>
            <el-form-item prop="newPassword">
        <el-input
          v-model="registerForm.newPassword"
          type="Password"
          size="large"
          auto-complete="off"
          placeholder="新密码"
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
          placeholder="重复新密码"
          @keyup.enter="handleRegister"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          size="large"
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="请输入右侧计算结果"
          style="width: 60%"
          @keyup.enter="handleRegister"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>
    </div>
      </div>
      <div>
        <br/>
      </div>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width:50%;margin-left:25%;marign-right:50%;"
          @click.prevent="handleRegister"
        >
          <span v-if="!loading">验证并重置</span>
          <span v-else>修 改 中...</span>
        </el-button>
      </el-form-item>
      <el-form-item style="width:100%;">
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
import { ref } from 'vue';
import { ElMessageBox } from "element-plus";
import { getCodeImg, register } from "@/api/login.js";
import { updateUserPwdDl ,sendMsg, sendSmsMsg} from "@/api/system/user.js";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { verify_type} = proxy.useDict("verify_type");
const registerForm = ref({
  username: "",
  newPassword: "",
  confirmPassword: "",
  code: "",
  uuid: "",
  identity:"",
  unit:"",
   emailCode: '',
   phonenumber:'',
    verifyType: ''

});

const sending = ref(false);
const countdown = ref(0); // 初始化倒计时变量

const sendVerificationCode = async () => {
    if (!registerForm.value.verifyType.trim()) {
        ElMessageBox.alert("请先选择验证方式", "提示", {
            type: "warning",
        });
        return;
    }
    var verifyType = registerForm.value.verifyType
    if (verifyType == '1') {// 短信验证
        if (!registerForm.value.phonenumber.trim()) {
            ElMessageBox.alert("手机号不能为空", "提示", {
                type: "warning",
            });
            return;
        }
        try {
            // 发送短信验证码的逻辑，这里需要根据您的后端接口进行调用
            const data = {
                username: registerForm.value.username,
                phonenumber: registerForm.value.phonenumber
            }
            sendSmsMsg(data).then(response => {
                sending.value = true; // 开始发送时禁用按钮
                countdown.value = 60; // 重置倒计时时间为60秒

                let intervalId = setInterval(() => {
                    if (countdown.value > 0) {
                        countdown.value--; // 每次减少1秒
                    } else {
                        clearInterval(intervalId); // 倒计时结束，清除定时器
                        sending.value = false; // 启用按钮
                        countdown.value = 0; // 重置倒计时
                    }
                }, 1000);
            });
        } catch (error) {
            console.error('发送验证码失败:', error);
            sending.value = false; // 如果发送失败，重新启用按钮
            clearInterval(intervalId); // 清除倒计时定时器
            countdown.value = 0; // 重置倒计时
        }
    } else {// 邮箱验证
        if (!registerForm.value.username.trim()) {
            ElMessageBox.alert("账号不能为空", "提示", {
                type: "warning",
            });
            return;
        }
        if (!registerForm.value.phonenumber.trim()) {
            ElMessageBox.alert("手机号不能为空", "提示", {
                type: "warning",
            });
            return;
        }
        try {
            // 发送邮箱验证码的逻辑，这里需要根据您的后端接口进行调用
            sendMsg(registerForm.value.username, registerForm.value.phonenumber).then(response => {
                sending.value = true; // 开始发送时禁用按钮
                countdown.value = 60; // 重置倒计时时间为60秒

                let intervalId = setInterval(() => {
                    if (countdown.value > 0) {
                        countdown.value--; // 每次减少1秒
                    } else {
                        clearInterval(intervalId); // 倒计时结束，清除定时器
                        sending.value = false; // 启用按钮
                        countdown.value = 0; // 重置倒计时
                    }
                }, 1000);
            });
        } catch (error) {
            console.error('发送验证码失败:', error);
            sending.value = false; // 如果发送失败，重新启用按钮
            clearInterval(intervalId); // 清除倒计时定时器
            countdown.value = 0; // 重置倒计时
        }
    }
};


 const { sys_user_identity } = proxy.useDict("sys_user_identity");
const equalToPassword = (rule, value, callback) => {
  if (registerForm.value.newPassword !== value) {
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
  code: [{ required: true, trigger: "change", message: "请输入验证码" }],

  emailCode: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

const codeUrl = ref("");

const loading = ref(false);
const captchaEnabled = ref(true);

function handleRegister() {
  proxy.$refs.registerRef.validate(valid => {
    if (valid) {
      loading.value = true;
      updateUserPwdDl(registerForm.value.username,registerForm.value.phonenumber, registerForm.value.newPassword, registerForm.value.code, registerForm.value.uuid, registerForm.value.emailCode).then(response => {
        const username = registerForm.value.username;
        ElMessageBox.alert("<font color='red'>恭喜你，账号 " + username + " 密码修改成功！</font>", "系统提示", {
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
};
// function submit() {
//   proxy.$refs.pwdRef.validate(valid => {
//     if (valid) {
//       updateUserPwdDl(registerForm.username,registerForm.phonenumber, registerForm.newPassword).then(response => {
//         proxy.$modal.msgSuccess("修改成功");
//       });
//     }
//   });
// };




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
// .input-with-button {
//   position: relative;
// }

.button-in-input {
  // position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  padding: 0 10px;
}
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
}
</style>
