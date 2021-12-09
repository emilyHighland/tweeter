package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import util.Pair;

import java.io.IOException;

public abstract class AuthenticationTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected String username;
    protected String password;
    protected User authenticatedUser;
    protected AuthToken authToken;

    public AuthenticationTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        Pair<User, AuthToken> loginResult = runAuthenticationTask();
        authenticatedUser = loginResult.getFirst();
        authToken = loginResult.getSecond();
        Cache.getInstance().setCurrUserAuthToken(authToken);
        BackgroundTaskUtils.loadImage(authenticatedUser);
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask() throws IOException, TweeterRemoteException;

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
