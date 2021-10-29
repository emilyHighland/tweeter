package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.FollowService;
import edu.byu.cs.tweeter.client.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowingTask.
 */
public class GetFollowingHandler extends PagedHandler<PagedObserver<User>> {

    public GetFollowingHandler(FollowService.FollowingObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get following";
    }
}

