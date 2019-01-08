package ar.com.mobeats.consolidar.backend.service.rest;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.TipoEntidad;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.service.CRUDService;
import ar.com.mobeats.consolidar.backend.service.CRUDServiceRest;
import ar.com.mobeats.consolidar.backend.service.TipoEntidadService;

@Service
@Path("/tipos-entidades")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoEntidadRestService extends CRUDServiceRest<TipoEntidad> {

	@Autowired
    private TipoEntidadService tipoEntidadService;
    
    @Override
    public CRUDService<TipoEntidad> getService() {
        return tipoEntidadService;
    }
    
    @POST
    @RolesAllowed("CREATE_TIPO-ENTIDAD")
    public Response createTipoEntidad(@Context HttpServletRequest request, TipoEntidad tipoEntidad) {
        return super.create(request, tipoEntidad);         
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("UPDATE_TIPO-ENTIDAD")
    public Response updateTipoEntidad(@Context HttpServletRequest request, TipoEntidad tipoEntidad) {
        return super.update(request, tipoEntidad);         
    }
 
    @DELETE
    @Path("{id}")
    @RolesAllowed("DELETE_TIPO-ENTIDAD")
    public Response deleteTipoEntidad(@Context HttpServletRequest request, @PathParam("id") String id) {
        return super.delete(request, id);            
    }
    
    @GET
    @Path("{id}")
    @RolesAllowed("READ_TIPO-ENTIDAD")
    public Response getTipoEntidad(@Context HttpServletRequest request, @PathParam("id") String id) {
        return super.get(request, id);   	
    }
 
    @GET
    @RolesAllowed("READ_TIPO-ENTIDAD")
    public Response find(@Context HttpServletRequest request,
            @QueryParam("codigo") String codigo,
            @QueryParam("valor") String valor,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize) {
        try {
            PageResponse<TipoEntidad> tiposEntidades = tipoEntidadService.find(codigo, valor, new PageRequest(page, pageSize));
            return responseHandler.buildSuccessResponse(tiposEntidades);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @GET
    @Path("all")
    @RolesAllowed("READ_TIPO-ENTIDAD")
    public Response getAll(@Context HttpServletRequest request) {
        return super.getAll(request);
    }

}
