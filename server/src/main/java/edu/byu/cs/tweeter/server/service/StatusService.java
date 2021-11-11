package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
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

    StoryDAO getStoryDAO(){
        return new StoryDAO();
    }
}
