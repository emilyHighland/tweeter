package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.observer.SimpleNotificationObserver;

public class PostStatusHandler extends SimpleNotificationHandler {

    public PostStatusHandler(SimpleNotificationObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to post status";
    }
}
