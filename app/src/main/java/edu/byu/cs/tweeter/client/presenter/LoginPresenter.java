package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.service.UserService;

public class LoginPresenter extends AuthenticatePresenter{

    private final UserService userService;

    public LoginPresenter(AuthenticateView view){
        super(view);
        userService = new UserService();
    }

    public void login(String alias, String password){

        String validationError = validateLogin(alias,password);
        if (validationError == null){
            getAuthenticateView().clearValidationError();
            view.displayInfoMessage("Logging In...");

            userService.login(alias, password, new GetLoginObserver());
        }
        else {
            getAuthenticateView().displayValidationError(validationError);
        }
    }

    private String validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    private class GetLoginObserver extends GetAuthenticateObserver implements UserService.LoginObserver {}
}
