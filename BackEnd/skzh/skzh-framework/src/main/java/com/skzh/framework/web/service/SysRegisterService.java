package com.skzh.framework.web.service;

import com.skzh.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.skzh.common.constant.CacheConstants;
import com.skzh.common.constant.Constants;
import com.skzh.common.constant.UserConstants;
import com.skzh.common.core.domain.entity.SysUser;
import com.skzh.common.core.domain.model.RegisterBody;
import com.skzh.common.core.redis.RedisCache;
import com.skzh.common.exception.user.CaptchaException;
import com.skzh.common.exception.user.CaptchaExpireException;
import com.skzh.common.utils.MessageUtils;
import com.skzh.common.utils.SecurityUtils;
import com.skzh.common.utils.StringUtils;
import com.skzh.framework.manager.AsyncManager;
import com.skzh.framework.manager.factory.AsyncFactory;
import com.skzh.system.service.ISysConfigService;
import com.skzh.system.service.ISysUserService;

import java.util.Date;

/**
 * 注册校验方法
 *
 * @author skzh
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "" , username = registerBody.getUsername(), password = registerBody.getPassword(),
                email = registerBody.getEmail(), phonenumber= registerBody.getPhonenumber(),
                identity = registerBody.getIdentity(),unit = registerBody.getUnit();


// 获取当前日期
        Date now = new Date();
// 加一个月
        Date trialPeriodEnd = DateUtils.addMonths(now, 1);


        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        SysUser sysUser1 = new SysUser();
        sysUser1.setPhonenumber(phonenumber);

        SysUser sysUser2 = new SysUser();
        sysUser2.setEmail(email);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else if (!userService.checkPhoneUnique(sysUser1)) {
            msg = "保存用户'" + username + "'失败，注册电话已存在";
        }
        else if (!userService.checkEmailUnique(sysUser2)) {
            msg = "保存用户'" + username + "'失败，注册邮箱已存在";
        }
        else {
//            sysUser.setNickName(username);
            sysUser.setEmail(email);
            sysUser.setPhonenumber(phonenumber);
            sysUser.setIdentity(identity);
            sysUser.setUnit(unit);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setRoleId(Long.valueOf("2"));
            sysUser.setTrialPeriodEnd(trialPeriodEnd);
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success" )));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "" );
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
