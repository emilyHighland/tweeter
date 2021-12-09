package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;

import java.io.IOException;
import java.util.Random;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthorizedTask {
    private static final String LOG_TAG = "IsFollowerTask";
    public static final String IS_FOLLOWER_KEY = "is-follower";

    private User follower;
    private User followee;
    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        IsFollowerRequest request = new IsFollowerRequest(authToken, follower.getAlias(), followee.getAlias());
        IsFollowerResponse response = SF.isFollower(request, "/isfollower");
        setIsFollower(response.isFollower());
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
            msgBundle.putBoolean(IS_FOLLOWER_KEY, getIsFollower());
    }

    private boolean getIsFollower() {
        return isFollower;
    }

    private void setIsFollower(boolean follower) {
        isFollower = follower;
    }
}
