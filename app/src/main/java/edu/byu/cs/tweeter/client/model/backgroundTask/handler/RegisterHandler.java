package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class RegisterHandler extends AuthenticateHandler {

    public RegisterHandler(UserService.RegisterObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to register";
    }
}
