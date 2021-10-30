package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.task.PagedTask;
import edu.byu.cs.tweeter.client.model.observer.PagedObserver;

public abstract class PagedHandler<T extends PagedObserver> extends BackgroundTaskHandler<T> {

    public PagedHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
