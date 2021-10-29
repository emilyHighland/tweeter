package edu.byu.cs.tweeter.client.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

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
    protected void runTask() throws IOException {
        count = runCountTask();
    }

    protected abstract int runCountTask();

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
