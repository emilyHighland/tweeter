package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response{

    private int followeeCount;

    private FollowingCountResponse(){}

    public FollowingCountResponse(int followeeCount) {
        super(true);
        this.followeeCount = followeeCount;
    }

    public int getFolloweeCount() {
        return followeeCount;
    }

    public void setFolloweeCount(int followeeCount) {
        this.followeeCount = followeeCount;
    }
}
