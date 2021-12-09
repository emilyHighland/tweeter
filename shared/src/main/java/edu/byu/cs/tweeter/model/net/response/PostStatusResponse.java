package edu.byu.cs.tweeter.model.net.response;

public class PostStatusResponse {

    private boolean successPosting;

    private PostStatusResponse(){}

    public PostStatusResponse(boolean successPosting) {
        this.successPosting = successPosting;
    }

    public boolean isSuccessPosting() {
        return successPosting;
    }

    public void setSuccessPosting(boolean successPosting) {
        this.successPosting = successPosting;
    }
}
