package ar.com.mobeats.consolidar.backend.service.rest;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UploadResponse;
import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.service.UploadFileService;
import ar.com.mobeats.consolidar.backend.service.UserService;
import ar.com.mobeats.consolidar.backend.util.RequestHandler;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@Service
@Path("/upload")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UploadDocRestService {

    @Autowired
    private RestResponseHandler responseHandler;

    @Value("${documents.dir}")
    private String directoryForDocs;
    
    @Value("${images.dir}")
    private String directoryForImages;
    
    @Autowired
	private RequestHandler requestHandler;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UploadFileService uploadFileService;

    @POST
    @Path("docs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDoc(@Context HttpServletRequest request, Attachment attachment) {
        try {
            String completeFileName = uploadFileService.saveFile(attachment, directoryForDocs);
            return responseHandler.buildSuccessResponse(
                    Arrays.asList(new UploadResponse(Status.OK.name(), completeFileName)));
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }
    
    @POST
    @Path("images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@Context HttpServletRequest request, Attachment attachment) {
        try {
        	UserData userData = requestHandler.getUserFromRequestInfoRequired(request);
        	String fileName = uploadFileService.saveImage(attachment, this.directoryForImages, new Long(1));
            return responseHandler.buildSuccessResponse(
            		new UploadResponse(Status.OK.name(), fileName));
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }
    
    @POST
    @Path("images/user/{userId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImageForUser(@Context HttpServletRequest request, 
    		Attachment attachment, @PathParam("userId") final String userId) {
        try {
        	User user = userService.find(userId);
        	String fileName = uploadFileService.saveImage(attachment, this.directoryForImages, user.getId());
            return responseHandler.buildSuccessResponse(
            		new UploadResponse(Status.OK.name(), fileName));
        } catch (Exception e) {
            return responseHandler.buildErrorResponse(e);
        }
    }
    
}
