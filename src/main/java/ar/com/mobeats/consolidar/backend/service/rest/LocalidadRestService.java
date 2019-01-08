package ar.com.mobeats.consolidar.backend.service.rest;

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

import ar.com.mobeats.consolidar.backend.dto.LocalidadDTO;
import ar.com.mobeats.consolidar.backend.model.Localidad;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.service.CRUDService;
import ar.com.mobeats.consolidar.backend.service.CRUDServiceRest;
import ar.com.mobeats.consolidar.backend.service.LocalidadService;

@Service
@Path("/localidades")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocalidadRestService extends CRUDServiceRest<Localidad>{
	
	@Autowired
	private LocalidadService localidadService;
    
    @POST
    public Response createLocalidad(@Context HttpServletRequest request, LocalidadDTO localidadDTO) {
        Localidad localidad = this.localidadService.createLocalidadFromDTO(localidadDTO);
        return responseHandler.buildResponse(localidad, Status.CREATED);
    }

    @PUT
    @Path("{id}")
    public Response updateLocalidad(@Context HttpServletRequest request, Localidad localidad) {
        return super.update(request, localidad);
    }

    @DELETE
    @Path("{id}")
    public Response deleteLocalidad(@Context HttpServletRequest request, @PathParam("id") String id) {
        return super.delete(request, id);
    }

    @GET
    @Path("all")
    public Response getAllLocalidad(@Context HttpServletRequest request) {
        return super.getAll(request);
    }

    @GET
    @Path("create-one-for-testing-purpose")
    public Response createOneForTestingPurpose(@Context HttpServletRequest request) {
        try {
            PageResponse<Localidad> localidades  = localidadService.createOneForTestingPurpose();
            return responseHandler.buildSuccessResponse(localidades);
        }
        catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @GET
    @Path("{id}")
    public Response getLocalidad(@Context HttpServletRequest request, @PathParam("id") String id) {
        return super.get(request, id);
    }

    @Override
    public CRUDService<Localidad> getService() {
        return this.localidadService;
    }

    @GET
    public Response findLocalidad(@Context HttpServletRequest request,
            @QueryParam("codigo") String codigo,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize) {
        try {
            PageResponse<Localidad> localidades = localidadService.find(codigo, new PageRequest(page, pageSize));
            return responseHandler.buildSuccessResponse(localidades);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }
}
