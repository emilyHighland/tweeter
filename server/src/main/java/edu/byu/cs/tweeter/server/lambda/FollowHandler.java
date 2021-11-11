package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowsRequest, SimpleResponse> {

    @Override
    public SimpleResponse handleRequest(FollowsRequest request, Context context) {
        FollowService service = new FollowService();
        return service.follow(request);
    }
}
