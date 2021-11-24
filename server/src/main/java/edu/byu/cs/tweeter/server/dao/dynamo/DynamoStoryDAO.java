package edu.byu.cs.tweeter.server.dao.dynamo;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.StoryDAOInterface;
import edu.byu.cs.tweeter.server.util.FakeData;

import java.util.List;

public class DynamoStoryDAO implements StoryDAOInterface {

    @Inject
    public DynamoStoryDAO() {}

    @Override
    public List<Status> getStory(String alias){
        // get list of 'alias' statuses

        return null;
    }

    // POST STATUS
    @Override
    public void postStatus(String alias, String post){

    }

    List<Status> getDummyStatuses(){
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
