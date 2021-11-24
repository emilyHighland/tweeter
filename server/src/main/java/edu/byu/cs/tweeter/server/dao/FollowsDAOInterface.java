package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowsDAOInterface {
    List<User> getFollowees(String alias);
    List<User> getFollowers(String alias);
    void follow(String alias);
    void unfollow(String alias);
}
