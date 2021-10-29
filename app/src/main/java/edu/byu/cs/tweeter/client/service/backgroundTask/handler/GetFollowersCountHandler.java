package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.observer.CountObserver;

public class GetFollowersCountHandler extends CountHandler {

    public GetFollowersCountHandler(CountObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get followers count";
    }
}
