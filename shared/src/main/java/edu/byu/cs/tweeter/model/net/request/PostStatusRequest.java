package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostStatusRequest {

    String post;
    AuthToken authToken;
    String alias;

    private PostStatusRequest(){}

    public PostStatusRequest(String post, AuthToken authToken, String alias) {
        this.post = post;
        this.authToken = authToken;
        this.alias = alias;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
