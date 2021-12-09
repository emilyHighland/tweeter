package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

import java.io.IOException;

public abstract class GetCountTask extends AuthorizedTask {

    public static final String COUNT_KEY = "count";

    /**
     * The user whose follower count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    protected int count;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        count = runCountTask();
    }

    protected abstract int runCountTask() throws IOException, TweeterRemoteException;

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
