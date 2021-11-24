package edu.byu.cs.tweeter.server.dao.dynamo;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.FeedDAOInterface;

import java.util.List;

public class DynamoFeedDAO implements FeedDAOInterface {

    @Inject
    public DynamoFeedDAO() {}

    @Override
    public List<Status> getFeed(String alias) {
        // return list of statuses (alias' feed)
        return null;
    }
}
