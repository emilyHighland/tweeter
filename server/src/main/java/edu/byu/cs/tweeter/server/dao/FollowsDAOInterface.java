package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowsDAOInterface {
    List<User> getFollowees(String alias, int limit, String lastFollowee);
    List<User> getFollowers(String alias, int limit, String lastFollower);
    void add(String followeeAlias, String currAlias, String followeeName, String currName, String followeeImage, String currUserImage);
    void delete(String currAlias, String followeeAlias);
    boolean find(String currAlias, String followeeAlias);
    List<String> getAllFollowerAliases(String followeeAlias);
    void addFollowersBatch(List<String> followers, String followeeTarget);
}
