package edu.byu.cs.tweeter.client.service.observer;

/**
 * Base Observer interface
 */
public interface ServiceObserver {
    void handleFailure(String message);
}
