package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

import java.io.IOException;

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
    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        Log.println(Log.DEBUG ,LOG_TAG,"RUN TASK "+ alias);
        GetUserRequest request = new GetUserRequest(authToken, alias);
        GetUserResponse response = SF.getUser(request, "/getuser");
        user = response.getUser();
        BackgroundTaskUtils.loadImage(user);
    }

    private User getUser() {
        return user;
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getUser());
    }
}
