package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import util.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public abstract class PagedTask<T> extends AuthorizedTask {
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    protected User targetUser;
    private int limit;
    private T lastItem;
    private List<T> items;
    private boolean hasMorePages;

    public PagedTask(AuthToken authToken, User targetUser, int limit, T lastItem, Handler messageHandler) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        Pair<List<T>, Boolean> pageOfUsers = getItems();
        items = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();
        loadImages(items);
    }

    protected abstract Pair<List<T>, Boolean> getItems() throws IOException, TweeterRemoteException;

    protected abstract List<User> getUsersForItems(List<T> items);

    private void loadImages(List<T> items) throws IOException {
        List<User> users = getUsersForItems(items);
        for (User u : users) {
            BackgroundTaskUtils.loadImage(u);
        }
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    public User getTargetUser() {
        return targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public T getLastItem() {
        return lastItem;
    }
}
