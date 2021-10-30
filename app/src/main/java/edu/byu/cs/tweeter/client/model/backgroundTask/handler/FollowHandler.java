package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class FollowHandler extends SimpleNotificationHandler {

    public FollowHandler(FollowService.FollowObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to follow";
    }
}
