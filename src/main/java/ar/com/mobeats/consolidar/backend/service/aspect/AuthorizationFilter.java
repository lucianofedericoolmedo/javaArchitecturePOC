package ar.com.mobeats.consolidar.backend.service.aspect;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.service.exception.UnauthorizedException;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;

@Service
public class AuthorizationFilter implements ContainerRequestFilter {
    
    @Context
    private HttpServletRequest request;
    
    @Autowired
    private RequestHandler requestHandler;
    
    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class);
    
    private List<PublicRule> publicMethodsRules;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            UserData userData = null;
            if (isPublicMethod(request.getMethod(), request.getRequestURI())) {
                userData = requestHandler.getUserFromRequestInfo(request);
            } else {                
                userData = requestHandler.verifyToken(request);
                if (userData == null || StringUtils.isEmpty(userData.getEmail())) {
                    throw new UnauthorizedException("Token verified, but User is invalid!");
                }
            }
            if (userData != null) {
                requestHandler.saveUserInRequestInfo(request, userData);
            }
        } catch (UnauthorizedException e) {
            LOGGER.warn("Exception in AuthorizationFilter", e);
            requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
        }
        
    }
    
    private boolean isPublicMethod(String httpMethod, String uri) {
        for (PublicRule publicRule : publicMethodsRules) {
            if (httpMethod.equals(publicRule.getHttpMethod())) {
                for (String endpoint : publicRule.getEndpoints()) {
                    if (uri.matches(endpoint)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public List<PublicRule> getPublicMethodsRules() {
        return publicMethodsRules;
    }
    
    public void setPublicMethodsRules(List<PublicRule> publicMethodsRules) {
        this.publicMethodsRules = publicMethodsRules;
    }
 
}
