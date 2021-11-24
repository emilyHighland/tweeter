package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<LogoutRequest, SimpleResponse> {

    @Override
    public SimpleResponse handleRequest(LogoutRequest logoutRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        UserService userService = injector.getInstance(UserService.class);
        return userService.logout(logoutRequest);
    }
}
