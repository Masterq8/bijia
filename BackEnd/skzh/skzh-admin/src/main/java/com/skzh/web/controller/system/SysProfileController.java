package com.skzh.web.controller.system;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.skzh.framework.web.service.SysRegisterService;
import com.skzh.quartz.util.EmailUtils;
import com.skzh.system.service.ISysConfigService;
import com.skzh.web.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.skzh.common.annotation.Log;
import com.skzh.common.config.SkzhConfig;
import com.skzh.common.core.controller.BaseController;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.common.core.domain.model.LoginUser;
import com.skzh.common.enums.BusinessType;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.common.utils.StringUtils;
import com.skzh.common.utils.file.FileUploadUtils;
import com.skzh.common.utils.file.MimeTypeUtils;
import com.skzh.framework.web.service.TokenService;
import com.skzh.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.aliyun.teautil.Common.toJSONString;


/**
 * 个人信息 业务处理
 *
 * @author skzh
 */
@RestController
@RequestMapping("/system/user/profile" )
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysRegisterService sysRegisterService;

    @Value("${aly_sms.signName}" )
    private String signName;

    @Value("${aly_sms.templateCode}" )
    private String templateCode;

    @Value("${aly_sms.accessKeyId}" )
    private String accessKeyId;

    @Value("${aly_sms.accessKeySecret}" )
    private String accessKeySecret;


    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup" , userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup" , userService.selectUserPostGroup(loginUser.getUsername()));

        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setUnit(user.getUnit());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在" );
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在" );
        }
        if (userService.updateUserProfile(currentUser) > 0) {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员" );
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd" )
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误" );
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同" );
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员" );
    }
    /**
     * 邮箱验证码
     */
//    @Log(title = "发送邮箱验证码" , businessType = BusinessType.UPDATE)
    @PostMapping("/sendMsg" )
    public AjaxResult sendMsg( String username, String phonenumber,HttpServletRequest request) {

        HttpSession session = request.getSession();
        SysUser sysUser = userService.selectUserByUserName(username);
        if (sysUser == null) {
            return error("发送验证码失败,用户不存在" );
        }
        String phonenumber1 = sysUser.getPhonenumber();
        if (!phonenumber.equals(phonenumber1)) {
            return error("发送验证码失败,电话号码错误" );
        }
        String email = sysUser.getEmail();//
        String emailCode= generateRandomPassword(6);//
        System.out.println(emailCode);
        EmailUtils.sendAuthCodeEmail(email,emailCode);//
        System.out.println("发送完毕");
        session.setAttribute("EMAIL_VERIFICATION_CODE",emailCode);
        return AjaxResult.success();
    }

    /**
     * 短信验证码
     */
//    @Log(title = "发送短信验证码" , businessType = BusinessType.UPDATE)
    @PostMapping("/sendSmsMsg" )
    public AjaxResult sendSmsMsg( String username, String phonenumber,HttpServletRequest request) {

        HttpSession session = request.getSession();
        SysUser sysUser = userService.selectUserByUserName(username);
        if (sysUser == null) {
            return error("发送验证码失败,用户不存在" );
        }
        String phonenumber1 = sysUser.getPhonenumber();
        if (!phonenumber.equals(phonenumber1)) {
            return error("发送验证码失败,用户手机号码错误" );
        }

        String emailCode= generateRandomPassword(6);
        System.out.println(emailCode);
        try {
            // 初始化请求客户端
            Client client = SmsUtil.createClient(accessKeyId, accessKeySecret);

            Map<String, String> param = new HashMap<>();
            param.put("code", emailCode);

            // 构造请求对象，请填入请求参数值
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phonenumber)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam(toJSONString(param));

            // 获取响应对象
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

            // 响应包含服务端响应的 body 和 headers
            System.out.println(toJSONString(sendSmsResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("短信验证码发送失败，请稍后再试！");
        }
        session.setAttribute("EMAIL_VERIFICATION_CODE",emailCode);
        return AjaxResult.success();
    }



    /**
     * 忘记密码
     */
//    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwdDl" )
    public AjaxResult updatePwdDl(String username,String phonenumber, String newPassword,String code,String uuid, String emailCode,HttpServletRequest request) {
        HttpSession session = request.getSession();
        String emailCode11 = (String) session.getAttribute("EMAIL_VERIFICATION_CODE");// 验证码校验
//        if (emailCode11 == null) {
//            return error("修改密码失败，未找到邮箱验证码");
//        }
        if (!emailCode.equals(emailCode11)) {
            return error("修改密码失败,邮箱验证码错误" );
        }
        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            sysRegisterService.validateCaptcha(username,code, uuid);
        }
        SysUser sysUser = userService.selectUserByUserName(username);
        if (sysUser == null) {
            return error("修改密码失败,用户不存在" );
        }
String phonenumber1 = sysUser.getPhonenumber();
//        String email = sysUser.getEmail();//
//        EmailUtils.sendAuthCodeEmail(email,emailCode);//
if (!phonenumber.equals(phonenumber1)) {
            return error("修改密码失败,电话号码错误" );
}

//
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(username, newPassword) > 0) {
            // 更新缓存用户密码
           sysUser.setPassword(newPassword);
            return success();
        }
        return error("修改密码异常，请联系管理员" );
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像" , businessType = BusinessType.UPDATE)
    @PostMapping("/avatar" )
    public AjaxResult avatar(@RequestParam("avatarfile" ) MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(SkzhConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl" , avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员" );
    }
    public static String generateRandomPassword(int length) {
        String characters = "1234567890";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}
