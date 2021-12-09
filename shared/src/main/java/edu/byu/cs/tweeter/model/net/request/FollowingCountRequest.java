package edu.byu.cs.tweeter.model.net.request;

public class FollowingCountRequest {

    private String alias;

    private FollowingCountRequest(){}

    public FollowingCountRequest(String userAlias) {
        this.alias = userAlias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
