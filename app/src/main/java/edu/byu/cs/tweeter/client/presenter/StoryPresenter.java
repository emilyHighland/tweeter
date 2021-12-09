package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public interface StoryView extends PagedView<Status>{}

    private final StatusService statusService;

    public StoryPresenter(StoryView view, User targetUser){
        super(view, targetUser);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(User targetUser, int PAGE_SIZE, Status lastItem) {
        statusService.getStory(targetUser, PAGE_SIZE, lastItem, new GetStoryObserver());
    }

    @Override
    protected String getDescription() {
        return "get story";
    }

    private class GetStoryObserver extends GetItemsObserver implements StatusService.StoryObserver {}
}