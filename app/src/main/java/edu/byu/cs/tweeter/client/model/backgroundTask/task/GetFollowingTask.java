package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import util.Pair;

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
        // request
        // response = SF.login()
        try {
            FollowingRequest request = new FollowingRequest(authToken, targetUser.alias, getLimit(),
                    getLastItem().alias);
            FollowingResponse response = SF.getFollowees(request,"/getfollowing/:authToken");
            return new Pair<>(response.getFollowees(), response.isSuccess());
        } catch (Exception e){
            e.printStackTrace();
            sendExceptionMessage(e);
        }
        return null;


//        return getFakeData().getPageOfUsers((User) getLastItem(), getLimit(), targetUser);
    }
}
