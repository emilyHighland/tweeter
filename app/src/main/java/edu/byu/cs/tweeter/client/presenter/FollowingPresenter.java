package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    private final FollowService followService;

    public FollowingPresenter(PagedView<User> view, User targetUser){
        super(view, targetUser);
        followService = new FollowService();
    }

    @Override
    protected void getItems(User targetUser, int PAGE_SIZE, User lastItem) {
        followService.getFollowing(targetUser, PAGE_SIZE, lastItem, new GetFollowingObserver());
    }

    @Override
    protected String getDescription() {
        return null;
    }

    private class GetFollowingObserver extends GetItemsObserver implements FollowService.FollowingObserver{}
}
