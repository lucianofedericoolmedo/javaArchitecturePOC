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

import ar.com.mobeats.consolidar.backend.dto.Credential;
import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.service.UserService;
import ar.com.mobeats.consolidar.backend.service.exception.RefreshTokenException;
import ar.com.mobeats.consolidar.backend.service.security.TokenHandler;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;
import ar.com.mobeats.consolidar.backend.util.SessionHandler;

@Service
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationRestService {

    @Autowired
    private UserService userService;

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private RequestHandler requestHandler;

    @POST
    @Path("/login")
    public Response login(@Context HttpServletRequest request, Credential credential) {
        try {
            User user = userService.login(credential.getUsername(), credential.getPassword());
            configureUser(user, request);
            return responseHandler.buildSuccessResponse(user);

        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    /*    @POST
    @Path("/register")
    public Response register(User newUser, @Context HttpServletRequest request) {
        try {
            User user = userService.register(newUser);
            configureUser(user, request);
            return responseHandler.buildSuccessResponse(user, Status.CREATED);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }*/
    
    @POST
    @Path("/refresh")
    public Response refreshToken(@Context HttpServletRequest request) {
        try {
            UserData user = requestHandler.getExpiredUserFromRequestInfo(request);
            String token = tokenHandler.createToken(user);
            return responseHandler.buildSuccessResponse(token);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(new RefreshTokenException(e));
        }
    }

    private void configureUser(User user, HttpServletRequest request) {
        String token = tokenHandler.createToken(UserData.fromUser(user));
        sessionHandler.saveUserInSession(request, token, user);
        user.setToken(token);
    }

}
