package edu.byu.cs.tweeter.client.model.observer;

/**
 * Base Observer interface
 */
public interface ServiceObserver {
    void handleFailure(String message);
}
