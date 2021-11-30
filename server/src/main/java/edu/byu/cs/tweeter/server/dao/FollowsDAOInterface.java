package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowsDAOInterface {
    List<User> getFollowees(String alias, int limit, String lastFollowee);
    List<User> getFollowers(String alias, int limit, String lastFollower);
    void follow(String alias);
    void unfollow(String alias);
}
