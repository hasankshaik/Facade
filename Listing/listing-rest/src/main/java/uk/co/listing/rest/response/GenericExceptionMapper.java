package uk.co.listing.rest.response;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import uk.co.listing.exceptions.CcsException;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(final Throwable ex) {
        if (ex instanceof WebApplicationException) {
            LOGGER.warn("WebApplicationException caught: " + ex.getLocalizedMessage());
            Status status = Status.fromStatusCode(((WebApplicationException) ex).getResponse().getStatus());
            if (status == null) {
                status = Status.fromStatusCode(500);
            }
            return ComplexResponse.buildResponseWithStatusAndMessage(status, ex.getMessage(), ex.getStackTrace());
        } else if (ex instanceof CcsException) {
            LOGGER.info("CcsException caught: " + ex.getLocalizedMessage());
            return ComplexResponse.buildBadRequestResponse(ex.getMessage());
        } else {
            LOGGER.error("Server exception occurred: " + ex.getLocalizedMessage(), ex);
            return ComplexResponse.buildServerErrorResponse("An error occurred");
        }
    }
}