package edu.byu.cs.tweeter.client.model.observer;

public interface IsFollowerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
