package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse {

    private boolean success;
    private boolean isFollower;

    private IsFollowerResponse(){};

    public IsFollowerResponse(boolean success, boolean isFollower) {
        this.success = success;
        this.isFollower = isFollower;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
