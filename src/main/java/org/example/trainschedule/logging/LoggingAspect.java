package org.example.trainschedule.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.trainschedule.service.BulkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* org.example.trainschedule.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        LOGGER.info("==> {}.{}() вызван с аргументами: {}", className, methodName, joinPoint.getArgs());

        try {
            Object result = joinPoint.proceed();
            LOGGER.info("<== {}.{}() успешно выполнен. Результат: {}", className, methodName, result);
            return result;
        } catch (Exception e) {
            LOGGER.error("<== {}.{}() завершился с ошибкой: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}