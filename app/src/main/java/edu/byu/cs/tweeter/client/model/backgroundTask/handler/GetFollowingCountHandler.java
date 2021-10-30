package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.observer.CountObserver;

public class GetFollowingCountHandler extends CountHandler {

    public GetFollowingCountHandler(CountObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get following count";
    }
}
