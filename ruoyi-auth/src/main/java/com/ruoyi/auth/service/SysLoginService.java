package com.ruoyi.auth.service;

import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.system.api.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLoginService {

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;



}
