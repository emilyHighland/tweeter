package edu.byu.cs.tweeter.client.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.service.UserService;

public class LogoutHandler extends SimpleNotificationHandler {

    public LogoutHandler(UserService.LogoutObserver observer){
        super(observer);
    }

    @Override
    protected String getFailedMessagePrefix() {
        return "Failed to logout";
    }
}
