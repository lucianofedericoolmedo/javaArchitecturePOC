package ar.com.mobeats.consolidar.backend.service.aspect;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.util.RequestHandler;

@Service
public class RequestInfoFlusher implements ContainerResponseFilter {
    
    private static final Logger CONSOLE_LOGGER = Logger.getLogger("requestInfoConsoleLogger");
    private static final Logger FILE_LOGGER = Logger.getLogger("requestInfoFileLogger");
    
    @Context
    private HttpServletRequest request;
    
    @Autowired
    private RequestHandler requestHandler;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String requestInfoJSON = null;

        RequestInfo requestInfo = requestHandler
                .getRequestInfoOrCreateNew(request);

        try {
            requestInfoJSON = requestInfo.toJSON();

        } catch (Exception e) {
            requestInfoJSON = "{\"error\" : \"unable to serialize request\"}";
        }            
            
        CONSOLE_LOGGER.debug("requestInfo = " + requestInfoJSON);
        FILE_LOGGER.info(requestInfoJSON);
    }

}
