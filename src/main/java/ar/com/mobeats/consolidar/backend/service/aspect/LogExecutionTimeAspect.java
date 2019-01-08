package ar.com.mobeats.consolidar.backend.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Aspect("perthis(@annotation(ar.com.mobeats.consolidar.backend.service.annotation.LogExecutionTime))")
@Component
@Scope("prototype")
public class LogExecutionTimeAspect {

    @Around("@annotation(ar.com.mobeats.consolidar.backend.service.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();
        Object proceed = pjp.proceed();
        long endTime = System.nanoTime();
        Long duration = (endTime - startTime) / 1000000; // Divide to get the result in miliseconds
        String result = String.format("Duration for '%s' method was %d ms.", pjp.getSignature().getName(), duration);
        System.out.println(result);
        return proceed;

    }
    
}
