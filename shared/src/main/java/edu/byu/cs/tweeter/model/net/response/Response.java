package edu.byu.cs.tweeter.model.net.response;

import java.io.Serializable;

/**
 * A base class for server responses.
 */
class Response implements Serializable {

    public Response(){}

    protected boolean success;
    protected String message;

    /**
     * Creates an instance with a null message.
     *
     * @param success the success message.
     */
    Response(boolean success) {
        this(success, null);
    }

    /**
     * Creates an instance.
     *
     * @param success the success indicator.
     * @param message the error message.
     */
    Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
