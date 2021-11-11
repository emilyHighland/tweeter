package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.util.FakeData;

public class UserDAO {

    public AuthToken authenticateToken(AuthToken authToken) throws TweeterRemoteException {
        assert authToken != null;
        return authToken;
    }

    public User authenticateUser(User user) throws TweeterRemoteException {
        assert user != null;
        return user;
    }

    public GetUserResponse getUser(GetUserRequest request){
        assert request.getAlias() != null;
        assert request.getAuthToken() != null;

        return new GetUserResponse(getFakeData().findUserByAlias(request.getAlias()));
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
