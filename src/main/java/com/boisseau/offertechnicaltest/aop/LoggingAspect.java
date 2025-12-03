package com.boisseau.offertechnicaltest.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String POINTCUT = "execution (* com.boisseau.offertechnicaltest.service..*(..))";

    @Before(POINTCUT)
    public void logMethodCall(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        String args = Arrays.toString(jp.getArgs());
        LOGGER.info("Method called: {}, with args: {}", methodName, args);
    }

    @AfterReturning(pointcut = POINTCUT, returning = "result")
    public void logMethodReturn(JoinPoint jp, Object result) {
        String methodName = jp.getSignature().getName();
        LOGGER.info("Method returned from {}, with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "exception")
    public void logMethodError(JoinPoint jp, Throwable exception) {
        String methodName = jp.getSignature().getName();
        LOGGER.error("Exception in method: {}, with message: {}",
                methodName,
                exception.getMessage(),
                exception
        );
    }
}
