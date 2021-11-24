package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {

    @Override
    public StoryResponse handleRequest(StoryRequest storyRequest, Context context) {
        Injector injector = InjectorConfig.getInstance().getInjector();
        StatusService statusService = injector.getInstance(StatusService.class);
        return statusService.getStory(storyRequest);
    }
}
