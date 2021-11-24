package edu.byu.cs.tweeter.server.dao.factories;

import edu.byu.cs.tweeter.server.dao.*;

public interface DAOFactoryInterface {
    UserDAOInterface getUserDAO();
    FeedDAOInterface getFeedDAO();
    StoryDAOInterface getStoryDAO();
    FollowsDAOInterface getFollowsDAO();
    AuthTokenDAOInterface getAuthTokenDAO();
    ImageDAOInterface getImageDAO();
}
