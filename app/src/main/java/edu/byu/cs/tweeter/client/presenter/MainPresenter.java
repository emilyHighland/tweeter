package edu.byu.cs.tweeter.client.presenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter{

    private static final String LOG_TAG = "MainPresenter";

    public interface MainView extends Presenter.View{
        void setIsFollower(boolean isFollower);
        void setVisibility(boolean isVisible);
        void updateFollowButton(boolean removed);
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void logoutUser();
    }

    private final MainView view;

    private UserService userService;
    private StatusService statusService;
    private FollowService followService;

    public MainPresenter(MainView view){
        super(view);
        this.view = view;
    }

    public UserService getUserService() {
        if (userService == null){
            userService = new UserService();
        }
        return userService;
    }

    public StatusService getStatusService() {
        if (statusService == null){
            statusService = new StatusService();
        }
        return statusService;
    }

    public FollowService getFollowService() {
        if (followService == null){
            followService = new FollowService();
        }
        return followService;
    }

    public void isFollower(User selectedUser){
        if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.setVisibility(false);
        } else {
            view.setVisibility(true);
            getFollowService().getIsFollowerTask(Cache.getInstance().getCurrUser(), selectedUser,
                    new IsFollowerObserver() {
                @Override
                public void handleSuccess(boolean isFollower) {
                    view.setIsFollower(isFollower);
                }

                @Override
                public void handleFailure(String message) {
                    view.displayErrorMessage(message);
                }
            });
        }
    }

    public void unfollow(User selectedUser){
        view.displayInfoMessage("Removing " + selectedUser.getName() + "...");
        getFollowService().getUnfollowTask(selectedUser, new FollowService.UnfollowObserver() {
            @Override
            public void handleSuccess() {
                view.clearInfoMessage();
                view.updateFollowButton(true);
                view.displayInfoMessage("Removed " + selectedUser.getName() + "...");
            }

            @Override
            public void handleFailure(String message) {
                view.clearInfoMessage();
                view.displayErrorMessage(message);
            }
        });
    }


    public void follow(User selectedUser){
        view.displayInfoMessage("Adding " + selectedUser.getName() + "...");
        getFollowService().getFollowTask(selectedUser, new FollowService.FollowObserver() {
            @Override
            public void handleSuccess() {
                view.clearInfoMessage();
                view.updateFollowButton(false);
                view.displayInfoMessage("Added " + selectedUser.getName() + "...");
            }

            @Override
            public void handleFailure(String message) {
                view.clearInfoMessage();
                view.displayErrorMessage(message);
            }
        });
    }


    public void logout(){
        view.displayInfoMessage("Logging Out...");

        getUserService().logout(new UserService.LogoutObserver() {
            @Override
            public void handleSuccess() {
                Cache.getInstance().clearCache();
                view.clearInfoMessage();
                view.logoutUser();
            }

            @Override
            public void handleFailure(String message) {
                view.clearInfoMessage();
                view.displayErrorMessage(message);
            }
        });
    }


    public void updateFollowingAndFollowersCount(User selectedUser){
        ExecutorService executor = Executors.newFixedThreadPool(2);

        getFollowService().getFollowerCount(selectedUser, executor, new CountObserver() {
            @Override
            public void handleFailure(String message) {
                view.displayErrorMessage(message);
            }

            @Override
            public void handleSuccess(int count) {
                view.setFollowerCount(count);
            }
        });

        getFollowService().getFollowingCount(selectedUser, executor, new CountObserver() {
            @Override
            public void handleFailure(String message) {
                view.displayErrorMessage(message);
            }

            @Override
            public void handleSuccess(int count) {
                view.setFollowingCount(count);
            }
        });
    }


    public void postStatus(String post){
        view.displayInfoMessage("Posting Status...");

        getStatusService().postStatus(post, Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser().alias, new StatusService.PostStatusObserver() {
            @Override
            public void handleSuccess() {
                view.clearInfoMessage();
                view.displayInfoMessage("Successfully Posted!");
            }

            @Override
            public void handleFailure(String message) {
                view.clearInfoMessage();
                view.displayErrorMessage(message);
            }
        });
    }
}
