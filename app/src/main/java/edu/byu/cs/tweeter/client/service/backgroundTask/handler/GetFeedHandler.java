package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.StatusService;
import edu.byu.cs.tweeter.client.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends PagedHandler<PagedObserver<Status>> {

    public GetFeedHandler(StatusService.FeedObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get feed";
    }
}
