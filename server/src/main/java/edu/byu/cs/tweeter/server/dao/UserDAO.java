package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

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
