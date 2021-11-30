package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public interface FeedDAOInterface {
    void addFeed(String alias, Status status);
    List<Status> getFeed(String alias, int limit, Status lastStatus);
    void updateFeed(String alias);
}
