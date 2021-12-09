package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;

import java.io.IOException;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthorizedTask {
    private static final String LOG_TAG = "UnfollowTask";
    public static final String UNFOLLOW_KEY = "unfollow";

    private User followee;
    private boolean unfollowed;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        User currUser = Cache.getInstance().getCurrUser();
        FollowsRequest request = new FollowsRequest(authToken, followee.getAlias(), currUser.getAlias(), followee.getName(),
                currUser.getName(), followee.getImageUrl(), currUser.getImageUrl());
        SimpleResponse response = SF.unfollow(request, "/unfollow");
        unfollowed = response.isSuccess();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putBoolean(UNFOLLOW_KEY, unfollowed);
    }
}
