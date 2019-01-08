package ar.com.mobeats.consolidar.backend.service.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.PasswordChangeDTO;
import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.dto.WrappedBoolean;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.service.CRUDService;
import ar.com.mobeats.consolidar.backend.service.CRUDServiceRest;
import ar.com.mobeats.consolidar.backend.service.UserService;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@Service
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestService extends CRUDServiceRest<User>{

	@Autowired
	private UserService userService;

	@Autowired
	private RestResponseHandler responseHandler;

	@Autowired
	private RequestHandler requestHandler;

	@POST
	@Path("/password/change")
	public Response changePassword(@Context HttpServletRequest request,
			PasswordChangeDTO passwordChange) {
		try {
			UserData user = requestHandler
					.getUserFromRequestInfoRequired(request);
			userService.changePassword(user.getUsername(), passwordChange);
			return responseHandler.buildSuccessResponse(Status.OK);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@RolesAllowed("READ_USER")
	public Response find(@Context HttpServletRequest request,
			@QueryParam("username") String username,
			@QueryParam("lastname") String lastname,
			@QueryParam("organizacion") String organizacionId,
			@QueryParam("page") Integer page,
			@QueryParam("pageSize") Integer pageSize) {
		try {
			PageResponse<UserData> users = userService.find(username, lastname,
					organizacionId, new PageRequest(page, pageSize));
			return responseHandler.buildSuccessResponse(users);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("{id}")
	@RolesAllowed("READ_USER")
	public Response find(@Context HttpServletRequest request,
			@PathParam("id") String id) {
		try {
			User user = userService.find(id);
			return responseHandler.buildSuccessResponse(user);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@POST
	@RolesAllowed("CREATE_USER")
	public Response createUser(@Context HttpServletRequest request, User newUser) {
		try {
			User user = userService.register(newUser);
			return responseHandler.buildResponse(user, Status.CREATED);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@PUT
    @Path("updateImpresora/{id}")
    @RolesAllowed("UPDATE_USER")
    public Response updateImpresora(@Context HttpServletRequest request, UserData user) {
        try {
            User userResult = userService.updateImpresora(user);
            return responseHandler.buildSuccessResponse(userResult);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

	@PUT
	@Path("{id}")
	@RolesAllowed("UPDATE_USER")
	public Response updateUser(@Context HttpServletRequest request, User user) {
		try {
			User userResult = userService.update(user);
			return responseHandler.buildSuccessResponse(userResult);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}
	
	@GET
	@Path("profile")
	@RolesAllowed("READ_PROFILE")
	public Response find(@Context HttpServletRequest request) {
		try {
			UserData userData = requestHandler
					.getUserFromRequestInfoRequired(request);
			User user = userService.find(userData.getId());
			return responseHandler.buildSuccessResponse(user);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("profile/image/{userId}")
	@Produces("image/jpeg")
	@RolesAllowed("READ_PROFILE")
	public StreamingOutput retrieveProfileImage(@Context final HttpServletRequest request,
			@PathParam("userId") final String userId) {
		
		return new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				InputStream imageStream = null;
				try {
					imageStream = userService.findImage(userId);
					IOUtils.copy(imageStream, output);
				} catch (Exception ex) {
					Logger.getLogger(UserRestService.class.getName()).log(Level.SEVERE, null, ex);
					Response errorResponse = responseHandler.buildErrorResponse(ex);
					throw new WebApplicationException(errorResponse);
				} finally {
					IOUtils.closeQuietly(imageStream);
				}
			}
		};
	}
	
	@PUT
	@Path("profile")
	@RolesAllowed("UPDATE_PROFILE")
	public Response updateProfile(@Context HttpServletRequest request, User profile) {
		try {
			UserData userData = requestHandler
					.getUserFromRequestInfoRequired(request);
			User userResult = userService.update(profile, userData);
			return responseHandler.buildSuccessResponse(userResult);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@DELETE
	@Path("{id}")
	@RolesAllowed("DELETE_USER")
	public Response deleteUser(@Context HttpServletRequest request,
			@PathParam("id") String id) {
		try {
			userService.delete(id);
			return responseHandler.buildSuccessResponse(Status.ACCEPTED);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("exists/{username}")
	@RolesAllowed("READ_USERNAME_EXISTS")
	public Response exists(@Context HttpServletRequest request,
			@PathParam("username") String username) {
		try {
			Boolean exists = userService.exists(username);
			return responseHandler.buildSuccessResponse(new WrappedBoolean(exists));
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}
	
	@GET
	@Path("all")
	@RolesAllowed("READ_USER")
	public Response allUser(@Context HttpServletRequest request){
	    return super.getAll(request);
	}

    @Override
    public CRUDService<User> getService() {
        return userService;
    }
}
