package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.FollowService;

public class FollowHandler extends SimpleNotificationHandler {

    public FollowHandler(FollowService.FollowObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to follow";
    }
}
