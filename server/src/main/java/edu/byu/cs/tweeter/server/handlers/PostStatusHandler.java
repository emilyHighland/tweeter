package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, SimpleResponse> {
    @Override
    public SimpleResponse handleRequest(PostStatusRequest postStatusRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        StatusService statusService = injector.getInstance(StatusService.class);
        return statusService.postStatus(postStatusRequest);
    }
}
