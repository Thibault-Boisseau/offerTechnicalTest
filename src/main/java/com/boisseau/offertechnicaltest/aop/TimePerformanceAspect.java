package com.boisseau.offertechnicaltest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimePerformanceAspect {

    public static final Logger LOGGER= LoggerFactory.getLogger(TimePerformanceAspect.class);

    private static final String POINTCUT = "execution (* com.boisseau.offertechnicaltest.service..*(..))";

    @Around(POINTCUT)
    public Object monitorTime(ProceedingJoinPoint jp) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return jp.proceed();
        } finally {
            long end = System.currentTimeMillis();
            LOGGER.info("Time taken by {}: {} ms",
                    jp.getSignature().getName(),
                    (end - start));
        }
    }
}
