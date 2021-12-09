package edu.byu.cs.tweeter.model.net.request;

public class FollowerCountRequest {

    private String alias;

    private FollowerCountRequest(){}

    public FollowerCountRequest(String userAlias) {
        this.alias = userAlias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


}
