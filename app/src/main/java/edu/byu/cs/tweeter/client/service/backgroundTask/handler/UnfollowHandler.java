package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.FollowService;

public class UnfollowHandler extends SimpleNotificationHandler {

    public UnfollowHandler(FollowService.UnfollowObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to unfollow";
    }
}
