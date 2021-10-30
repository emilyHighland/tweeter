package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status>{

    private final StatusService statusService;

    public FeedPresenter(PagedView<Status> view, User targetUser){
        super(view, targetUser);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(User targetUser, int PAGE_SIZE, Status lastItem) {
        statusService.getFeed(targetUser, PAGE_SIZE, lastItem, new GetFeedObserver());
    }

    @Override
    protected String getDescription() {
        return null;
    }

    private class GetFeedObserver extends GetItemsObserver implements StatusService.FeedObserver{}
}
