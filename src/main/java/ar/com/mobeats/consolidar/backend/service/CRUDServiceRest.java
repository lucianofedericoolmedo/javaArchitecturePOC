package ar.com.mobeats.consolidar.backend.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@Transactional
public abstract class CRUDServiceRest<T> {

    @Autowired
    protected RestResponseHandler responseHandler;

    public abstract CRUDService<T> getService();

    @Transactional
    protected Response create(HttpServletRequest request, T model) {
        try {
            model = this.getService().create(model);
            System.out.println("CREANDO LA CLASE");
            return responseHandler.buildResponse(model, Status.CREATED);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @Transactional
    protected Response update(HttpServletRequest request, T model) {
        try {
            model = this.getService().update(model);
            return responseHandler.buildResponse(model, Status.ACCEPTED);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @Transactional
    protected Response delete(HttpServletRequest request, String id) {
        try {
            T model = this.getService().get(id);
            this.getService().delete(model);
            return responseHandler.buildSuccessResponse(Status.ACCEPTED);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @Transactional
    protected Response get(HttpServletRequest request, String id) {
        try {
            this.getService().getLogger().info("Obtener entidad con id " + id);
            T comprobante = this.getService().get(id);
            return responseHandler.buildSuccessResponse(comprobante);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @Transactional
    protected Response find(@Context HttpServletRequest request, PageResponse<T> entidad) {
        try {
            return responseHandler.buildSuccessResponse(entidad);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

    @Transactional
    protected Response getAll(HttpServletRequest request) {
        try {
            List<T> models = this.getService().findAll();
            return responseHandler.buildSuccessResponse(models);
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }

}
