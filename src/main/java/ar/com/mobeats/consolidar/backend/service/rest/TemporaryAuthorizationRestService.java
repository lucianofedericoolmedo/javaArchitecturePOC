package ar.com.mobeats.consolidar.backend.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.service.UserService;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@Service
@Path("/authorization")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TemporaryAuthorizationRestService {

    @Autowired
    private UserService userService;

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private RequestHandler requestHandler;

    @POST
    @Path("/temporary")
    public Response generateTemporaryAuthorization(@Context HttpServletRequest request) {
        try {
            UserData user = requestHandler.getUserFromRequestInfoRequired(request);
            String temporaryKey = userService.generateTemporaryAuthorization(user.getId());
            return responseHandler.buildSuccessResponse(temporaryKey);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }
}
