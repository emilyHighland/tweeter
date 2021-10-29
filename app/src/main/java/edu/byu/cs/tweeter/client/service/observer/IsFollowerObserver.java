package edu.byu.cs.tweeter.client.service.observer;

public interface IsFollowerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
