package ar.com.mobeats.consolidar.backend.service.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.Rol;
import ar.com.mobeats.consolidar.backend.service.PermissionService;
import ar.com.mobeats.consolidar.backend.service.RoleService;
import ar.com.mobeats.consolidar.backend.service.exception.NotFoundException;
import ar.com.mobeats.consolidar.backend.service.exception.UnauthorizedException;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@RunWith(MockitoJUnitRunner.class)
public class SeguridadRestServiceTest {

	private SeguridadRestService service;
	
	@Mock
    private RestResponseHandler responseHandler;
	@Mock
	private RoleService roleService;
    @Mock
    private PermissionService permissionService;
    
	@Before
	public void setUp() throws Exception {
		service = new SeguridadRestService();
		service.setResponseHandler(responseHandler);
		service.setRoleService(roleService);
		service.setPermissionService(permissionService);
	}

	@Test
	public void createRoleSuccessfulReturnsStatusCREATED() {
		Rol expectedRole = buildRole(1L);
		Response expectedResponse = Response.status(Status.CREATED).entity(expectedRole).build();
		Rol role = buildRole(null);
		
		when(roleService.create(role))
			.thenReturn(expectedRole);
		when(responseHandler.buildResponse(expectedRole, Status.CREATED))
			.thenReturn(expectedResponse);
		
		Response response = service.createRole( null, role );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createRoleFailedReturnsErrorStatus() {
		Response expectedResponse = Response.status(Status.UNAUTHORIZED).build();
		Rol role = buildRole(null);
		
		when(roleService.create(role))
			.thenThrow(UnauthorizedException.class);
		when(responseHandler.buildErrorResponse(any(UnauthorizedException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.createRole( null, role );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void updateRoleSuccessfulReturnsStatusACCEPTED() {
		Rol role = buildRole(1L);
		Response expectedResponse = Response.status(Status.ACCEPTED).entity(role).build();
		
		when(roleService.update(role))
			.thenReturn(role);
		when(responseHandler.buildResponse(role, Status.ACCEPTED))
			.thenReturn(expectedResponse);
		
		Response response = service.updateRole(null, role);
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateRoleFailedReturnsErrorStatus() {
		Response expectedResponse = Response.status(Status.UNAUTHORIZED).build();
		Rol role = buildRole(null);
		
		when(roleService.update(role))
			.thenThrow(UnauthorizedException.class);
		when(responseHandler.buildErrorResponse(any(UnauthorizedException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.updateRole( null, role );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void deleteRoleSuccessfulReturnsStatusACCEPTED() {
		Long id = 1L;
		Rol role = buildRole(id);
		Response expectedResponse = Response.status(Status.ACCEPTED).build();
		
		when(roleService.get(id)).thenReturn(role);
		doNothing().when(roleService).delete(role);
		when(responseHandler.buildSuccessResponse(Status.ACCEPTED))
			.thenReturn(expectedResponse);
		
		Response response = service.deleteRole(null, id );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void deleteRoleFailedReturnsErrorStatus() {
		Long id = 1L;
		Rol role = buildRole(id);
		Response expectedResponse = Response.status(Status.UNAUTHORIZED).build();
		
		when(roleService.get(id)).thenReturn(role);
		doThrow(UnauthorizedException.class).when(roleService).delete(role);
		when(responseHandler.buildErrorResponse(any(UnauthorizedException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.deleteRole( null, id );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void getRoleSuccessfulReturnsStatusOK() {
		Long id = 1L;
		Rol role = buildRole(id);
		Response expectedResponse = Response.status(Status.OK).entity(role).build();
		
		when(roleService.get(id)).thenReturn(role);
		when(responseHandler.buildSuccessResponse(role))
			.thenReturn(expectedResponse);
		
		Response response = service.find(null, id );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getRoleFailedReturnsErrorStatus() {
		Long id = 1L;
		Response expectedResponse = Response.status(Status.NOT_FOUND).build();
		
		when(roleService.get(id)).thenThrow(NotFoundException.class);
		when(responseHandler.buildErrorResponse(any(NotFoundException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.find( null, id );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void getRolesSuccessfulReturnsStatusOK() {
		String nombre = "un nombre";
		List<Rol> roles = buildRoles();
		Response expectedResponse = Response.status(Status.OK).entity(roles).build();
		
		when(roleService.find(nombre)).thenReturn(roles);
		when(responseHandler.buildSuccessResponse(roles))
			.thenReturn(expectedResponse);
		
		Response response = service.findAll( null, nombre );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getRolesFailedReturnsErrorStatus() {
		String nombre = "un nombre";
		Response expectedResponse = Response.status(Status.NOT_FOUND).build();
		
		when(roleService.find(nombre)).thenThrow(NotFoundException.class);
		when(responseHandler.buildErrorResponse(any(NotFoundException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.findAll( null, nombre );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void getPermisosSuccessfulReturnsStatusOK() {
		List<Permiso> permissions = buildPermissions();
		Response expectedResponse = Response.status(Status.OK).entity(permissions).build();
		
		when(permissionService.findAll()).thenReturn(permissions);
		when(responseHandler.buildSuccessResponse(permissions))
			.thenReturn(expectedResponse);
		
		Response response = service.getPermisos(null);
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getPermisosFailedReturnsErrorStatus() {
		Response expectedResponse = Response.status(Status.NOT_FOUND).build();
		
		when(permissionService.findAll()).thenThrow(NotFoundException.class);
		when(responseHandler.buildErrorResponse(any(NotFoundException.class)))
			.thenReturn(expectedResponse);
		
		Response response = service.getPermisos( null );
		
		assertEquals(expectedResponse, response);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}
	
	private List<Permiso> buildPermissions() {
		List<Permiso> permissions = new ArrayList<Permiso>();
		permissions.add(buildPermission((long) 1));
		permissions.add(buildPermission((long) 2));
		
		return permissions;
	}

	private Permiso buildPermission(Long id) {
		Permiso permiso = new Permiso();
		permiso.setId(id);
		permiso.setNombre("un nombre");
		
		return permiso;
	}

	private List<Rol> buildRoles() {
		List<Rol> roles = new ArrayList<Rol>();
		roles.add(buildRole((long) 1));
		roles.add(buildRole((long) 2));
		
		return roles;
	}
	
	private Rol buildRole(Long id) {
		Rol role = new Rol();
		role.setId(id);
		role.setNombre("nuevo rol");
		role.setPermisos(new HashSet<Permiso>());
		
		return role;
	}

}
