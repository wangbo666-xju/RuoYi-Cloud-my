package com.ruoyi.auth.service;

import com.ruoyi.common.core.constant.CacheConstants;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.common.security.util.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysPasswordService {
    @Autowired
    private RedisService redisService;

    private int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;

    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    @Autowired
    private SysRecordLogService recordLogService;

    public String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password) {
        if (user == null || StringUtils.isBlank(password)) {
            return;
        }
        String userName = user.getUserName();
        Integer retryCount = redisService.getCacheObject(getCacheKey(userName));
        if (retryCount == null) {
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime);
            recordLogService.recordLogininfor(userName, Constants.LOGIN_FAIL, errMsg);
            throw new ServiceException(errMsg);
        }
        if (!matches(user, password)) {
            retryCount++;
            recordLogService.recordLogininfor(userName, Constants.LOGIN_FAIL, String.format("密码输入错误%s次", retryCount));
            redisService.setCacheObject(getCacheKey(userName), retryCount);
            throw new ServiceException("用户不存在/密码错误");
        } else {
            clearLoginRecordCache(userName);
        }

    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisService.hasKey(getCacheKey(loginName))) {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
