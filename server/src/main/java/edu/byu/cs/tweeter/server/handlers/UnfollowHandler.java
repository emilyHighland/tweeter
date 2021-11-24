package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnfollowHandler implements RequestHandler<FollowsRequest, SimpleResponse> {
    @Override
    public SimpleResponse handleRequest(FollowsRequest followsRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        FollowService followService = injector.getInstance(FollowService.class);
        return followService.unfollow(followsRequest);
    }
}
