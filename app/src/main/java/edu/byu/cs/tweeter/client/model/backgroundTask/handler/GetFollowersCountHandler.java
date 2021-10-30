package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.observer.CountObserver;

public class GetFollowersCountHandler extends CountHandler {

    public GetFollowersCountHandler(CountObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get followers count";
    }
}
