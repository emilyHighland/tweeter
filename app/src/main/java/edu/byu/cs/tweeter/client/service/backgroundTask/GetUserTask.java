package edu.byu.cs.tweeter.client.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthorizedTask {
    private static final String LOG_TAG = "GetUserTask";

    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;
    }

    @Override
    protected void runTask() {
        // Only needed because it will have actual code when we aren't using dummy data
    }

    private User getUser() {
        User user = getFakeData().findUserByAlias(alias);
        return user;
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getUser());
    }
}
