package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.LoginTask;
import edu.byu.cs.tweeter.client.model.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateHandler<T extends AuthenticateObserver> extends BackgroundTaskHandler<T> {

    public AuthenticateHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
        observer.handleSuccess(authToken, loggedInUser);
    }
}
