package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.sqs.DTO.UserDTO;
import edu.byu.cs.tweeter.server.util.Pair;

import java.util.List;

public interface UserDAOInterface {
    void addUser(String alias, String firstName, String lastName, String url, String hashedPassword, int followerCount, int followeeCount);
    Pair<User,String> getUserByID(String alias);
    void updateUser(String alias, String updateAttribute, int updatedValue);
    int getFollowingCount(String alias);
    int getFollowerCount(String alias);
}
