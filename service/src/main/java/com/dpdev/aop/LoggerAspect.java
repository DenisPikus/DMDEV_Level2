package com.dpdev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("within(com.dpdev.service.*Service)")
    public void serviceLayer() {
    }

    @Around("serviceLayer()")
    public Object addLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("invoked method {}() in class {}, with parameter {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("invoked method {}() in class {}, result {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName(), result);
            return result;
        } catch (Throwable ex) {
            log.info("invoked method {}() method in class {}, exception {}: {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName(), ex.getClass(), ex.getMessage());
            throw ex;
        }
    }
}
