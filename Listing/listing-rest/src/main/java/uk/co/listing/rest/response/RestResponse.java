package uk.co.listing.rest.response;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class RestResponse {
    private String errorMessage;
    private String successMessage;
    private final Map<String, Object> data = new HashMap<String, Object>();

    public RestResponse() {
        super();
    }

    public RestResponse(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RestResponse(final String key, final Object dataObject) {
        data.put(key, dataObject);
    }

    public Boolean hasErrorMsg() {
        return StringUtils.isNotBlank(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        successMessage = null;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(final String successMessage) {
        this.successMessage = successMessage;
        errorMessage = null;
    }

    public void setData(final Object dataObject) {
        data.put("data", dataObject);
    }

    public Map<String, Object> getData() {
        return data;
    }

}
