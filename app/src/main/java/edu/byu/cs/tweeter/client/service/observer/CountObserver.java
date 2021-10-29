package edu.byu.cs.tweeter.client.service.observer;

public interface CountObserver extends ServiceObserver {
    void handleSuccess(int count);
}
