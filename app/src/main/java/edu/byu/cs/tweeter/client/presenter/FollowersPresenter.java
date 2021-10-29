package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User>{

    private final FollowService followService;

    public FollowersPresenter(PagedView<User> view, User targetUser){
        super(view, targetUser);
        followService = new FollowService();
    }

    @Override
    protected void getItems(User targetUser, int PAGE_SIZE, User lastItem) {
        followService.getFollowers(targetUser, PAGE_SIZE, lastItem, new GetFollowersObserver());
    }

    @Override
    protected String getDescription() {
        return null;
    }

    private class GetFollowersObserver extends GetItemsObserver implements FollowService.FollowersObserver{}
}

