package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.UserService;

/**
 * Message handler (i.e., observer) for LoginTask (watches the task)
 */
public class LoginHandler extends AuthenticateHandler {

    public  LoginHandler(UserService.LoginObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to login";
    }
}
