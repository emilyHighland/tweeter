package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import util.Pair;

import java.io.IOException;

public abstract class AuthenticationTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected String username;
    /**
     * The user's password.
     */
    protected String password;

    protected User authenticatedUser;
    protected AuthToken authToken;

    public AuthenticationTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void runTask() throws IOException {
        Pair<User, AuthToken> loginResult = runAuthenticationTask();
        authenticatedUser = loginResult.getFirst();
        authToken = loginResult.getSecond();
        BackgroundTaskUtils.loadImage(authenticatedUser);
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask();

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
