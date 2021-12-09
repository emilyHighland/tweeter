package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowsRequest {

    private AuthToken authToken;
    private String followeeAlias;
    private String followerAlias;
    private String followeeName;
    private String followerName;
    private String followeeImage;
    private String followerImage;

    private FollowsRequest(){}

    public FollowsRequest(AuthToken authToken, String followeeAlias, String currUserAlias,
                          String followeeName, String currUserName, String followeeImage, String currUserImage) {
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
        this.followerAlias = currUserAlias;
        this.followeeName = followeeName;
        this.followerName = currUserName;
        this.followeeImage = followeeImage;
        this.followerImage = currUserImage;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeName() {
        return followeeName;
    }

    public void setFolloweeName(String followeeName) {
        this.followeeName = followeeName;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFolloweeImage() {
        return followeeImage;
    }

    public void setFolloweeImage(String followeeImage) {
        this.followeeImage = followeeImage;
    }

    public String getFollowerImage() {
        return followerImage;
    }

    public void setFollowerImage(String followerImage) {
        this.followerImage = followerImage;
    }
}
