package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public interface StoryDAOInterface {
    List<Status> getStory(String alias);
    void postStatus(String alias, String post);
}
