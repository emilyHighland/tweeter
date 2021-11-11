package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusService {


    public StoryResponse getStory(StoryRequest request){
        try {
            return getStoryDAO().getStory(request);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }

    public FeedResponse getFeed(FeedRequest request){
        try {
            return getFeedDAO().getFeed(request);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }

    public SimpleResponse postStatus(PostStatusRequest request){
        try {
            return getStoryDAO().postStatus(request);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }

    StoryDAO getStoryDAO(){
        return new StoryDAO();
    }

    FeedDAO getFeedDAO(){
        return new FeedDAO();
    }
}
