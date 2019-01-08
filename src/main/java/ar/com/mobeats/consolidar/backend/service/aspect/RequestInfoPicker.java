package ar.com.mobeats.consolidar.backend.service.aspect;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.util.RequestHandler;

@Service
public class RequestInfoPicker implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;
    
    @Autowired
    private RequestHandler requestHandler;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        RequestInfo requestInfo = requestHandler.getRequestInfoOrCreateNew(request);
        requestInfo.setStart(new Date());
        requestInfo.setHttpMethod(request.getMethod());
        requestInfo.setIpAddress(request.getRemoteAddr());
        requestInfo.setEndpoint(request.getRequestURI());
        requestInfo.setHeader(buildHeader(request));
        
        requestHandler.saveRequestInfo(request, requestInfo);
    }
    
    private Map<String, String> buildHeader(HttpServletRequest request) {
        
        Map<String, String> header = new HashMap<String, String>();
        
        Enumeration<String> keys = request.getHeaderNames();
        if (keys != null) {
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                if (!key.equals("authorization")) {
                    header.put(key, request.getHeader(key));
                }
            }
        }
        
        return header;
    }

}
