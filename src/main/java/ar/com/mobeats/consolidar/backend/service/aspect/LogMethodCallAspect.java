package ar.com.mobeats.consolidar.backend.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ar.com.mobeats.consolidar.backend.util.LoggerFactory;

@Aspect
@Component
@Scope("prototype")
public class LogMethodCallAspect {

    @Before("@annotation(ar.com.mobeats.consolidar.backend.service.annotation.LogMethodCall)")
    public void logExecutionTime(JoinPoint joinPoint) throws Throwable {
        String signatureName = joinPoint.getSignature().getName();
        Object targetObject = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        
        String logMessage = String.format("%s.%s(%s)", 
                targetObject.getClass().getName(),
                signatureName,
                args.toString());
        
        LoggerFactory.getLogger(targetObject).info(logMessage);
    }
    
}