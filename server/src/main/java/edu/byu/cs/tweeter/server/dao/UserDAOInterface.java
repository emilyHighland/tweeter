package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.util.Pair;

public interface UserDAOInterface {
    void addUser(String alias, String firstName, String lastName, String url, String salt, String hashed,
                 int followerCount, int followeeCount);
    Pair<User,String> getUserByID(String alias);
    void updateUser(String alias, String updateAttribute, String updatedValue);
    void deleteUser(String alias);
    String getUsersSalt();
}
