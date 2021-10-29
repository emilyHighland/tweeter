package edu.byu.cs.tweeter.client.presenter;

/**
 * Super presenter class.
 * TODO: whats common in ALL the presenters
 * whats common to all views goes in the base presenter
 */
public class Presenter {

    public interface View {
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    protected final View view;

    protected Presenter(View view){
        this.view = view;
    }


}
