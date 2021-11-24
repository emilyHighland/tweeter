package edu.byu.cs.tweeter.server.dao.factories;

import edu.byu.cs.tweeter.server.dao.*;
import edu.byu.cs.tweeter.server.dao.dynamo.*;

public class DynamoDBFactory implements DAOFactoryInterface {
    @Override
    public UserDAOInterface getUserDAO() {
        return new DynamoUserDAO();
    }

    @Override
    public FeedDAOInterface getFeedDAO() {
        return new DynamoFeedDAO();
    }

    @Override
    public StoryDAOInterface getStoryDAO() {
        return new DynamoStoryDAO();
    }

    @Override
    public FollowsDAOInterface getFollowsDAO() {
        return new DynamoFollowsDAO();
    }

    @Override
    public AuthTokenDAOInterface getAuthTokenDAO() {
        return new DynamoAuthTokenDAO();
    }

    @Override
    public ImageDAOInterface getImageDAO() {
        return new S3DAO();
    }
}
