package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFollowerCountHandler implements RequestHandler<FollowerCountRequest, FollowerCountResponse> {
    @Override
    public FollowerCountResponse handleRequest(FollowerCountRequest followerCountRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        UserService userService = injector.getInstance(UserService.class);
        return userService.getFollowerCount(followerCountRequest);
    }
}
