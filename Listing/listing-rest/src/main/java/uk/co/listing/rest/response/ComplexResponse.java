package uk.co.listing.rest.response;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

/**
 * Use this class to build a Response together with the response codes and a
 * data in to the entity, Call the static methods to return the appropriate
 * response objects with the HTTP status codes
 *
 * @author rvinayak
 *
 */
public class ComplexResponse {
    private static final Logger LOGGER = Logger.getLogger(ComplexResponse.class);

    private ComplexResponse() {

    }

    
    // create a response with passed in status code, message and data
    public static Response buildResponseWithStatusAndMessage(final Status status, final String msg, final Object data) {
        LOGGER.debug("Building response, " +status.getReasonPhrase() + ": " + msg + ", status code: " +status.getStatusCode());
        final ResponseBuilder builder = Response.status(status.getStatusCode());
        final boolean error = status.getStatusCode() >= 400;
        final ResponseEntity respEntity = new ResponseEntity(status.getStatusCode(), status.getReasonPhrase() + ": " + msg, data, error);
        return builder.entity(respEntity).build();
    }

    // 200(OK) with content
    public static Response buildOkResponse(final Object data) {
        return buildOkResponse(data, "success");
    }

    // 200(OK) with content
    public static Response buildOkResponse(final Object data, final String msg) {
        LOGGER.debug("Building response, status code: " +Status.OK);
        final ResponseEntity respEntity = new ResponseEntity(Status.OK.getStatusCode(), msg, data, false);
        final ResponseBuilder builder = Response.ok(respEntity);
        return builder.build();
    }

    // 201(CREATED)
    public static Response buildCreatedResponse() {
        return buildCreatedResponse("");
    }

    // 201(CREATED)
    public static Response buildCreatedResponse(final String uri) {
        LOGGER.debug("Building response, status code: " +Status.CREATED);
        ResponseBuilder builder = null;
        try {
            builder = Response.created(new URI(uri));
        } catch (final URISyntaxException e) {
            LOGGER.error(e);
            builder = Response.serverError();
        }
        return builder.build();
    }

    // 204(NO_CONTENT)
    public static Response buildOkResponseNoContent() {
        return Response.noContent().build();
    }

    // 400 (BAD_REQUEST)
    public static Response buildBadRequestResponse(final String msg) {
        return buildResponseWithStatusAndMessage(Status.BAD_REQUEST, msg, null);
    }

    // 401 (UNAUTHORIZED)
    public static Response buildUnathorizedResponse(final String msg) {
        return buildResponseWithStatusAndMessage(Status.UNAUTHORIZED, msg, null);
    }

    // 404 (Not Found) exception.
    public static Response buildNotFoundResponse(final String message) {
        return buildResponseWithStatusAndMessage(Status.NOT_FOUND, message, null);
    }

    // 500 (SERVER_ERROR)
    public static Response buildServerErrorResponse(final String msg) {
        return buildResponseWithStatusAndMessage(Status.INTERNAL_SERVER_ERROR, msg, null);
    }
}

class ResponseEntity {
    private int respCode;
    private boolean error;
    private String message;
    private Object data;

    public ResponseEntity(final int respCode, final String message, final Object data, final boolean error) {
        this.respCode = respCode;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(final int respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }
}
