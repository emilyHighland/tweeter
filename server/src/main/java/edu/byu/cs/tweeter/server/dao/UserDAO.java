package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;

public class UserDAO {

    public AuthToken authenticateToken(AuthToken authToken) throws TweeterRemoteException {
        assert authToken != null;
        return authToken;
    }

    public User authenticateUser(User user) throws TweeterRemoteException {
        assert user != null;
        return user;
    }
}
