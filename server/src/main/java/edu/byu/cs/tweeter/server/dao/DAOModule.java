package edu.byu.cs.tweeter.server.dao;

import com.google.inject.AbstractModule;
import edu.byu.cs.tweeter.server.dao.dynamo.*;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.factories.DynamoDBFactory;

public class DAOModule extends AbstractModule {

    public DAOModule() {}

    @Override
    protected void configure(){
        bind(DAOFactoryInterface.class).to(DynamoDBFactory.class);
        bind(UserDAOInterface.class).to(DynamoUserDAO.class);
        bind(FeedDAOInterface.class).to(DynamoFeedDAO.class);
        bind(StoryDAOInterface.class).to(DynamoStoryDAO.class);
        bind(FollowsDAOInterface.class).to(DynamoFollowsDAO.class);
        bind(AuthTokenDAOInterface.class).to(DynamoAuthTokenDAO.class);
        bind(ImageDAOInterface.class).to(S3DAO.class);
    }



//    @Provides
//    @Named("AmazonDynamoDB")
//    public DynamoDB amazonDynamoDB(){
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                .withRegion("us-east-2")
//                .build();
//        DynamoDB dynamoDB = new DynamoDB(client);
//        return dynamoDB;
//    }
}
