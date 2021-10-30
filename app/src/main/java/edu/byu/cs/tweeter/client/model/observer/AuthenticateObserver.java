package edu.byu.cs.tweeter.client.model.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateObserver extends ServiceObserver {
    void handleSuccess(AuthToken authToken, User user);
}
