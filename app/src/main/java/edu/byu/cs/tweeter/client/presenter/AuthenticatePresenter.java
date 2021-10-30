package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends Presenter{

    public interface AuthenticateView extends View{
        void displayValidationError(String message);
        void clearValidationError();
        void navigateToUser(User user);
    }

    protected AuthenticatePresenter(AuthenticateView view) {
        super(view);
    }

    protected AuthenticateView getAuthenticateView(){
        return (AuthenticateView) view;
    }

    private AuthenticateView getView(){
        return (AuthenticateView) view;
    }

    protected class GetAuthenticateObserver implements AuthenticateObserver{

        @Override
        public void handleSuccess(AuthToken authToken, User user) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.clearInfoMessage();
            getView().navigateToUser(user);
            view.displayInfoMessage("Hello " + user.getName());
        }

        @Override
        public void handleFailure(String message) {
            view.clearInfoMessage();
            view.displayErrorMessage(message);
        }
    }
}
