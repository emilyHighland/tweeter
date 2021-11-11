package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import util.Pair;

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
    protected Pair<List<User>, Boolean> getItems() {
        try {
            FollowersRequest request;
            if (getLastItem() == null){
                request = new FollowersRequest(authToken, targetUser.alias, getLimit(),
                        null);
            } else {
                request = new FollowersRequest(authToken, targetUser.alias, getLimit(),
                        getLastItem().alias);
            }
            FollowersResponse followersResponse = SF.getFollowers(request,"/getfollowers");
            return new Pair<>(followersResponse.getFollowers(), followersResponse.isSuccess());
        } catch (Exception e){
            e.printStackTrace();
            sendExceptionMessage(e);
        }
        return null;
    }
}
