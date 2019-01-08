package ar.com.mobeats.consolidar.backend.service.aspect;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;
import ar.com.mobeats.consolidar.backend.util.SessionHandler;

@Service
@Aspect
public class SecureAnnotationsInterceptor {
	
    @Autowired
    private RequestHandler requestHandler;
    
    @Autowired
    private SessionHandler sessionHandler;
	
    @Around("execution(* ar.com.mobeats.consolidar.backend.service.rest..*.*(..))")
    public Object checkRolesAllowed(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	boolean allowed = true;
    	Collection<String> permisosAutorizados = getAllRolesAllowed(joinPoint);
    	if (CollectionUtils.isNotEmpty(permisosAutorizados)) {
    		HttpServletRequest request = requestHandler.getRequest(joinPoint);
    		UserData userData = requestHandler.getUserFromRequestInfo(request);
    		if (userData == null) {
    			allowed = false;
    		} else {
    			String token = requestHandler.getToken(request);
    			User user = sessionHandler.loadUserFromSession(request, token, userData.getId());
    			allowed = (user == null) ? false : user.tieneAlgunPermiso(permisosAutorizados);
    		}
    	}
    	
    	if (!allowed) {
    		return Response.status(Status.FORBIDDEN).build();
    	}

        Object returnValue = joinPoint.proceed();
        return returnValue;
    }
    
    private Collection<String> getAllRolesAllowed(ProceedingJoinPoint joinPoint) {
    	
    	MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    	Class<?> clazz = methodSignature.getDeclaringType();
    	
    	RolesAllowed rolsAllowedAtClassLevel = clazz.getAnnotation(RolesAllowed.class);
    	RolesAllowed rolsAllowedAtMethodLevel = methodSignature.getMethod().getAnnotation(RolesAllowed.class);
    	
    	Collection<String> rols = concat(new HashSet<String>(), rolsAllowedAtClassLevel);
    	rols = concat(rols, rolsAllowedAtMethodLevel);
    	
    	return rols;
    }
    
    private Collection<String> concat(Collection<String> collection, RolesAllowed rolesAllowed) {
    	if (rolesAllowed != null) {
    		String[] rols = rolesAllowed.value();
    		if (rols != null) {
    			for (String rol : rols) {
    				collection.add(rol);
    			}
    		}
    	}
    	return collection;
    }
}
