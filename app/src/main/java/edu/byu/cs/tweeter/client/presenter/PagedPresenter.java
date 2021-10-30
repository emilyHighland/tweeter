package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    private static final String LOG_TAG = "PagedPresenter";

    protected static final int PAGE_SIZE = 10;

    public interface PagedView<U> extends View {
        void addLoadingFooter();
        void removeLoadingFooter();
        void displayMoreItems(List<U> items);
        void navigateToUser(User user);
    }

    protected User targetUser;
    protected T lastItem;
    protected boolean hasMorePages = true;
    protected boolean isLoading = false;
    protected boolean isGettingUser = false;
    private final UserService userService;

    public boolean isLoading(){
        return isLoading;
    }

    protected PagedPresenter(PagedView<T> view, User targetUser) {
        super(view);
        userService = new UserService();
        this.targetUser = targetUser;
    }

    protected PagedView<T> getPagedView(){
        return (PagedView<T>) view;
    }

    private PagedView getView(){
        return (PagedView) view;
    }

    public void loadMoreItems(){
        if (!isLoading && hasMorePages) {
            isLoading = true;
            getView().addLoadingFooter();
            getItems(targetUser, PAGE_SIZE, lastItem);
        }
    }

    public void getUser(String alias){
        if (!isGettingUser) {
            this.isGettingUser = true;
            getView().addLoadingFooter();
            getView().displayInfoMessage("Getting user's profile...");

            userService.getUser(alias, new GetUserObserver() {
                @Override
                public void handleSuccess(User user) {
                    getPagedView().clearInfoMessage();
                    getPagedView().navigateToUser(user);
                    isGettingUser = false;
                }

                @Override
                public void handleFailure(String message) {
                    view.clearInfoMessage();
                    view.displayErrorMessage(message);
                    isGettingUser = false;
                }
            });
        }
    }

    protected class GetItemsObserver implements PagedObserver<T>{
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            PagedPresenter.this.hasMorePages = hasMorePages;
            PagedPresenter.this.lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;

            getView().removeLoadingFooter();
            getView().displayMoreItems(items);
            isLoading = false;
        }

        @Override
        public void handleFailure(String message) {
            getView().removeLoadingFooter();
            getView().displayErrorMessage(message);
            isLoading = false;
        }
    }

    protected abstract void getItems(User targetUser, int PAGE_SIZE, T lastItem);

    protected abstract String getDescription();

}
