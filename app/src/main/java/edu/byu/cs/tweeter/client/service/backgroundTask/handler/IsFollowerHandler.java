package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.service.observer.IsFollowerObserver;

public class IsFollowerHandler<T extends IsFollowerObserver> extends BackgroundTaskHandler<T> {

    public IsFollowerHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to determine following relationship";
    }
}
