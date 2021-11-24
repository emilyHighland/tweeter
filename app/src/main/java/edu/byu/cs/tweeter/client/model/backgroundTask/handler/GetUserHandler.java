package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetUserTask;
import edu.byu.cs.tweeter.client.model.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler<T extends UserObserver> extends BackgroundTaskHandler<T> {

    public GetUserHandler(T observer){
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get user's profile";
    }
}
