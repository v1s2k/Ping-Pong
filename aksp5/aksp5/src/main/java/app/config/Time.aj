package app.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
public aspect Time {
    pointcut allServiceMethods(): within(ru.mirea.services.*);

    around(ProceedingJoinPoint proceedingJoinPoint): allServiceMethods() {
        MethodSignature methodSignature =
                (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long start = System.currentTimeMillis();
        Object res = null;
        try {
            res = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long fin = System.currentTimeMillis();

        log.info("Method: {} in {}, Work time: {} ms",
                methodName, className, fin - start);
        return res;
    }
}