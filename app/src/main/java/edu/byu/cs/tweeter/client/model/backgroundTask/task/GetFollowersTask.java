package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {
    private static final String LOG_TAG = "GetFollowersTask";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        FollowersRequest request;
        if (getLastItem() == null){
            request = new FollowersRequest(authToken, targetUser.alias, getLimit(),
                    null);
        } else {
            request = new FollowersRequest(authToken, targetUser.alias, getLimit(),
                    getLastItem().alias);
        }
        FollowersResponse followersResponse = SF.getFollowers(request, "/getfollowers");

        return new Pair<>(followersResponse.getFollowers(), followersResponse.isSuccess());
    }
}
