package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public interface FeedDAOInterface {
    List<Status> getFeed(String alias);
}
