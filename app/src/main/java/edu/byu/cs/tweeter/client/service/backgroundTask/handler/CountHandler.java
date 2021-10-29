package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.service.observer.CountObserver;

public abstract class CountHandler<T extends CountObserver> extends BackgroundTaskHandler<T> {

    public CountHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data){
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }

}
