package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PagedStatusTask extends PagedTask<Status> {

    public PagedStatusTask(AuthToken authToken, User targetUser, int limit, Status lastItem,
                           Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected List<User> getUsersForItems(List<Status> statuses) {
        return statuses.stream().map(x -> x.user).collect(Collectors.toList());
    }
}
