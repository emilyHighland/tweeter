package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAOInterface {
    void addAuthToken(AuthToken authToken, String alias);
    String getAuthToken(String alias);
    void deleteAuthToken(String authToken);
}
