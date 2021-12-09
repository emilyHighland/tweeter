package edu.byu.cs.tweeter.model.net.response;

public class FollowerCountResponse extends Response{

    private int followerCount;
    private FollowerCountResponse(){}

    public FollowerCountResponse(int followerCount) {
        super(true);
        this.followerCount = followerCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
