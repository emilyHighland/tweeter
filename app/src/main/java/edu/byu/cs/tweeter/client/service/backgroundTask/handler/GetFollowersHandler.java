package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.FollowService;
import edu.byu.cs.tweeter.client.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends PagedHandler<PagedObserver<User>> {

    public GetFollowersHandler(FollowService.FollowersObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get followers";
    }
}
