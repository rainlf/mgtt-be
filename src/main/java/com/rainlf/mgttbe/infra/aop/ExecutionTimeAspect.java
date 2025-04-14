package com.rainlf.mgttbe.infra.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutionTimeAspect {
    // 定义切入点：所有被@ExecutionTime注解的方法
    @Pointcut("@annotation(com.rainlf.mgttbe.infra.aop.ExecutionTime)")
    public void executionTimePointcut() {}

    @Around("executionTimePointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String clazz = signature.getDeclaringType().getSimpleName();
        String method = signature.getName();

        // 初始化监控数据
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed(); // 执行目标方法
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            log.info("{}.{}() execution time: {}ms", clazz, method, costTime);
        }
    }
}