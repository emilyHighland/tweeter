package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public interface StoryDAOInterface {
    void addStatus(String alias, Status status, long UNIXmillis);
    List<Status> getStory(String alias, int limit, Status lastStatus, long lastTimestamp);
}
