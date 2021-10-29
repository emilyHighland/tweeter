package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.observer.CountObserver;

public class GetFollowingCountHandler extends CountHandler {

    public GetFollowingCountHandler(CountObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get following count";
    }
}
