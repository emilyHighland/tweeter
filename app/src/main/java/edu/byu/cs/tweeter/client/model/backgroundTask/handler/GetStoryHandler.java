package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends PagedHandler<PagedObserver<Status>> {

    public GetStoryHandler(StatusService.StoryObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to get story";
    }
}
