package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;

import java.io.IOException;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthorizedTask {
    private static final String LOG_TAG = "UnfollowTask";

    /**
     * The user that is being followed.
     */
    private User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException {
        try {
            FollowsRequest request = new FollowsRequest(authToken, followee.getAlias());
            SimpleResponse response = SF.unfollow(request, "/unfollow");
        } catch (Exception e){
            e.printStackTrace();
            sendExceptionMessage(e);
        }
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {

    }
}
