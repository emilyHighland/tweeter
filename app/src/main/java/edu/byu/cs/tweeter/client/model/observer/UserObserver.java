package edu.byu.cs.tweeter.client.model.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserObserver extends ServiceObserver {
    void handleSuccess(User user);
}
