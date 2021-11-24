package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.observer.SimpleNotificationObserver;

/** empty handleSuccess() method */
public abstract class SimpleNotificationHandler<T extends SimpleNotificationObserver> extends BackgroundTaskHandler<T>{

    public SimpleNotificationHandler(T observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        observer.handleSuccess();
    }
}
