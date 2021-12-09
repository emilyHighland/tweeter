package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

import java.io.IOException;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {
    private static final String LOG_TAG = "LogoutTask";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected int runCountTask() throws IOException, TweeterRemoteException {
        FollowingCountRequest request = new FollowingCountRequest(targetUser.getAlias());
        FollowingCountResponse response = SF.getFollowingCount(request, "/getfollowingcount");

        return response.getFolloweeCount();
    }
}
