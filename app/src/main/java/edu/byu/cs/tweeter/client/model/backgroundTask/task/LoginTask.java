package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {
    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        try {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = SF.login(request, "/login");
            User loggedInUser = response.getUser();
            AuthToken authToken = response.getAuthToken();
            return new Pair<>(loggedInUser, authToken);
        } catch (Exception e){
            e.printStackTrace();
            sendExceptionMessage(e);
        }
        return null;
    }
}
