package ar.com.mobeats.consolidar.backend.service.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.mobeats.consolidar.backend.service.exception.ForbiddenException;
import ar.com.mobeats.consolidar.backend.service.exception.NotFoundException;
import ar.com.mobeats.consolidar.backend.service.exception.UnauthorizedException;
import ar.com.mobeats.consolidar.backend.util.RestResponseHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/commons.xml", "/spring/errors.xml"})
@ActiveProfiles("localhost")
public class RestResponseHandlerTest {

    @Autowired
	private RestResponseHandler restResponseHandler;
    
	@Test
	public void givenAnValidReq_ReturnResponseWithSuccess() {

		Response response = restResponseHandler
				.buildSuccessResponse(Status.OK);

		Assert.assertTrue(response.getStatus() == Status.OK
				.getStatusCode());
	}


	@Test
	public void givenResourceNotFound_ReturnResponseWithNotFound() {

		Response response = restResponseHandler
				.buildErrorResponse(new NotFoundException(), false);

		Assert.assertTrue(response.getStatus() == Status.NOT_FOUND
				.getStatusCode());
	}
	
	@Test
	public void givenAnUnauthorizedUser_ReturnResponseWithUnauthorized() {

		Response response = restResponseHandler
				.buildErrorResponse(new UnauthorizedException(), false);

		Assert.assertTrue(response.getStatus() == Status.UNAUTHORIZED
				.getStatusCode());
	}
	
	@Test
	public void givenAnUnknownError_ReturnResponseWithInternalServerError() {

		Response response = restResponseHandler
				.buildErrorResponse(new Exception(), false);

		Assert.assertTrue(response.getStatus() == Status.INTERNAL_SERVER_ERROR
				.getStatusCode());
	}

    @Test
    public void givenAnForbidden_ReturnResponseWithForbidden() {

        Response response = restResponseHandler
                .buildErrorResponse(new ForbiddenException(), false);

        Assert.assertTrue(response.getStatus() == Status.FORBIDDEN
                .getStatusCode());
    }
	
}
