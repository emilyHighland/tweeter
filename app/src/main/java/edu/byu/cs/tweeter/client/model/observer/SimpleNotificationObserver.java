package edu.byu.cs.tweeter.client.model.observer;

/** Sub-class of ServiceObserver
 * use this interface when don't need to pass any data just notify */
public interface SimpleNotificationObserver extends ServiceObserver {
    void handleSuccess();
}
