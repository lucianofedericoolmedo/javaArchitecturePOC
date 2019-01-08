package ar.com.mobeats.consolidar.backend.service.rest;

import java.util.List;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.Organizacion;
import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.Rol;
import ar.com.mobeats.consolidar.backend.service.OrganizacionService;
import ar.com.mobeats.consolidar.backend.service.PermissionService;
import ar.com.mobeats.consolidar.backend.service.RoleService;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@Service
@Path("/seguridad")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadRestService {

	@Autowired
	private RestResponseHandler responseHandler;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private OrganizacionService organizacionService;

	@POST
	@Path("roles")
	@RolesAllowed("CREATE_ROL")
	public Response createRole(@Context HttpServletRequest request, Rol role) {
		try {
			role = roleService.create(role);
			return responseHandler.buildResponse(role, Status.CREATED);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@PUT
	@Path("roles/{id}")
	@RolesAllowed("UPDATE_ROL")
	public Response updateRole(@Context HttpServletRequest request, Rol role) {
		try {
			role = roleService.update(role);
			return responseHandler.buildResponse(role, Status.ACCEPTED);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@DELETE
	@Path("roles/{id}")
	@RolesAllowed("DELETE_ROL")
	public Response deleteRole(@Context HttpServletRequest request,
			@PathParam("id") Long id) {
		try {
			Rol role = roleService.get(id);
			roleService.delete(role);
			return responseHandler.buildSuccessResponse(Status.ACCEPTED);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("roles/{id}")
	@RolesAllowed("READ_ROL")
	public Response find(@Context HttpServletRequest request,
			@PathParam("id") Long id) {
		try {
			Rol role = roleService.get(id);
			return responseHandler.buildSuccessResponse(role);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("roles")
	@RolesAllowed("READ_ROL")
	public Response findAll(@Context HttpServletRequest request,
			@QueryParam("nombre") String nombre) {
		try {
			List<Rol> roles = roleService.find(nombre);
			return responseHandler.buildSuccessResponse(roles);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("permisos")
	@RolesAllowed("READ_PERMISOS")
	public Response getPermisos(@Context HttpServletRequest request) {
		try {
			List<Permiso> permissions = permissionService.findAll();
			return responseHandler.buildSuccessResponse(permissions);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}

	@GET
	@Path("organizaciones")
	@RolesAllowed("READ_ORGANIZACIONES")
	public Response getOrganizaciones(@Context HttpServletRequest request){
		try {
			List<Organizacion> organizaciones = organizacionService.findAll();
			return responseHandler.buildSuccessResponse(organizaciones);
		} catch (Exception e) {
			return responseHandler.buildErrorResponse(e);
		}
	}
	
	public RestResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public void setResponseHandler(RestResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public PermissionService getPermissionService() {
		return permissionService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
