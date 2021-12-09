package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {
    private static final String LOG_TAG = "GetFeedTask";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        FeedRequest request;
        if (getLastItem() == null){
            request = new FeedRequest(authToken, targetUser.alias, getLimit(),
                    null);
        } else {
            request = new FeedRequest(authToken, targetUser.alias, getLimit(),
                    getLastItem());
        }
        FeedResponse feedResponse = SF.getFeed(request, "/getfeed");

        return new Pair<>(feedResponse.getStatuses(), feedResponse.isSuccess());
    }
}
