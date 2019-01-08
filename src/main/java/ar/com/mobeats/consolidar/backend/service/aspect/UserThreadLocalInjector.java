package ar.com.mobeats.consolidar.backend.service.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.service.security.UserThreadLocal;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;

@Service
@Aspect
public class UserThreadLocalInjector {
	
    @Autowired
    private RequestHandler requestHandler;
    
    private static final Class<?>[] AUDITABLE_METHODS = {POST.class, PUT.class, DELETE.class};

    @Around("execution(* ar.com.mobeats.consolidar.backend.service.rest..*.*(..))")
    public Object inject(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	UserData userData = getUserData(joinPoint);
		if (userData != null && isAuditableMethod(joinPoint)) {
			UserThreadLocal.set(userData.getId());
		} else {
			UserThreadLocal.unset();
		}
		
        Object returnValue = joinPoint.proceed();
        return returnValue;
    }
    
    private UserData getUserData(ProceedingJoinPoint joinPoint) {
		HttpServletRequest request = requestHandler.getRequest(joinPoint);
		UserData userData = requestHandler.getUserFromRequestInfo(request);
		return userData;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean isAuditableMethod(ProceedingJoinPoint joinPoint) {
    	MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    	Method method = methodSignature.getMethod();
    	for (Class httpMethod : AUDITABLE_METHODS) {
    		if (method.getAnnotation(httpMethod) != null) {
    			return true;
    		}
    	}
    	return false;
    }
}
