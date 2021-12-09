package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;

import java.io.IOException;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {
    private static final String LOG_TAG = "LogoutTask";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected int runCountTask() throws IOException, TweeterRemoteException {
        FollowerCountRequest request = new FollowerCountRequest(targetUser.getAlias());
        FollowerCountResponse response = SF.getFollowerCount(request, "/getfollowercount");

        return response.getFollowerCount();
    }
}
