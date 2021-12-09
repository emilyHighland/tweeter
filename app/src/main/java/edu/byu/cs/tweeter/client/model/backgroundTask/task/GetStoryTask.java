package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {
    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        StoryRequest request;
        if (getLastItem() == null){
            request = new StoryRequest(authToken, targetUser.alias, getLimit(),
                    null);
        } else {
            request = new StoryRequest(authToken, targetUser.alias, getLimit(),
                    getLastItem());
        }
        StoryResponse storyResponse = SF.getStory(request, "/getstory");

        return new Pair<>(storyResponse.getStatuses(), storyResponse.isSuccess());
    }
}