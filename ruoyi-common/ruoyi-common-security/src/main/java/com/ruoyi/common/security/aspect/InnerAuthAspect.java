package com.ruoyi.common.security.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InnerAuthAspect implements Ordered {



    @Override
    public int getOrder() {
        return 0;
    }
}
