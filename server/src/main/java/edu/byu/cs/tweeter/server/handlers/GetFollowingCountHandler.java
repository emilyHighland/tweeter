package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {

    @Override
    public FollowingCountResponse handleRequest(FollowingCountRequest followingCountRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        UserService userService = injector.getInstance(UserService.class);
        return userService.getFollowingCount(followingCountRequest);
    }
}
