package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import util.Pair;

import java.util.List;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {
    private static final String LOG_TAG = "GetFollowingTask";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {

        try {
            FollowingRequest request;
            if (getLastItem() == null){
                request = new FollowingRequest(authToken, targetUser.alias, getLimit(),
                        null);
            } else {
                request = new FollowingRequest(authToken, targetUser.alias, getLimit(),
                        getLastItem().alias);
            }
            FollowingResponse followingResponse = SF.getFollowees(request,"/getfollowing");
            return new Pair<>(followingResponse.getFollowees(), followingResponse.isSuccess());
        } catch (Exception e){
            e.printStackTrace();
            sendExceptionMessage(e);
        }
        return null;
    }
}
