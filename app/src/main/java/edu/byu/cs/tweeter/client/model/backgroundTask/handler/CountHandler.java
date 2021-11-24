package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.observer.CountObserver;

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
