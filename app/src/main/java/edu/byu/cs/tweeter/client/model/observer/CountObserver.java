package edu.byu.cs.tweeter.client.model.observer;

public interface CountObserver extends ServiceObserver {
    void handleSuccess(int count);
}
