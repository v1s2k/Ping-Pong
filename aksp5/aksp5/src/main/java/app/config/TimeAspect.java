package app.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeAspect {
    @Pointcut("within(app.services.*)")
    public void allServiceMethods() {
    }

    @Around("allServiceMethods()")
    public Object timeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature =
                (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long start = System.currentTimeMillis();
        Object res = proceedingJoinPoint.proceed();
        long fin = System.currentTimeMillis();

        log.info("Method: {} in {}, work time: {} ms",
                methodName, className, fin - start);
        return res;
    }
}