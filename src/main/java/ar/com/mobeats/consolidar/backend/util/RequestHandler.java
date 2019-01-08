package ar.com.mobeats.consolidar.backend.util;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.service.aspect.RequestInfo;
import ar.com.mobeats.consolidar.backend.service.exception.UnauthorizedException;
import ar.com.mobeats.consolidar.backend.service.security.TokenHandler;

@Service
public class RequestHandler {
    
    private static final String REQUEST_INFO_KEY = "requestInfo";
    
    @Autowired
    private TokenHandler tokenHandler;
    
    public UserData verifyToken(HttpServletRequest request) {
        String token = getToken(request);
        return tokenHandler.verifyToken(token);
    }

	public UserData tryToVerifyToken(HttpServletRequest request) {
        try {
            return verifyToken(request);
        } catch (Exception e) {
            return null;
        }
    }
    
    public UserData getUserFromRequestInfo(HttpServletRequest request) {
        RequestInfo requestInfo = getRequestInfo(request);
        return (requestInfo == null) ? null : requestInfo.getUser();
    }

    public UserData getUserFromRequestInfoRequired(HttpServletRequest request) {
        UserData user = getUserFromRequestInfo(request);
        if (user == null) {
            throw new UnauthorizedException("User is required");
        }
        return user;
    }
    
    public UserData getExpiredUserFromRequestInfo(HttpServletRequest request) {
        String token = getToken(request);
        return tokenHandler.getExpiredUser(token);
    }
    
    public void saveUserInRequestInfo(HttpServletRequest request, UserData user) {
        RequestInfo requestInfo = getRequestInfoOrCreateNew(request);
        requestInfo.setUser(user);
        request.setAttribute(REQUEST_INFO_KEY, requestInfo);
    }
    
    public RequestInfo getRequestInfoOrCreateNew(HttpServletRequest request) {
        RequestInfo requestInfo = getRequestInfo(request);
        if (requestInfo == null) {
            requestInfo = new RequestInfo();
        }
        return requestInfo;
    }
    
    public void saveRequestInfo(HttpServletRequest request, RequestInfo requestInfo) {
        request.setAttribute(REQUEST_INFO_KEY, requestInfo);
    }
    
    private RequestInfo getRequestInfo(HttpServletRequest request) {
        return (RequestInfo) request.getAttribute(REQUEST_INFO_KEY);
    }
    
    public HttpServletRequest getRequest(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    return (HttpServletRequest) arg;
                }
            }
        }
        return null;
    }
    
    public String getToken(HttpServletRequest request) {
        String value = request.getHeader("Authorization");
        if (StringUtils.isBlank(value)) {
            throw new UnauthorizedException("Token not found");
        }
        String valueTrimmed = value.trim();
        int index = valueTrimmed.indexOf(" ");
        if (index == -1) {
            throw new UnauthorizedException("Invalid Token");
        }
        String tokenType = valueTrimmed.substring(0, index);
        if (!"Bearer".equals(tokenType)) {
            throw new UnauthorizedException(
                    MessageFormat.format("Token {0} not supported",
                            tokenType));
        }
        String token = valueTrimmed.substring(index + 1);
        return token;
    }
    
}
