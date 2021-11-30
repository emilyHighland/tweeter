package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.StoryDAOInterface;
import edu.byu.cs.tweeter.server.util.FakeData;

import java.util.List;

public class DynamoStoryDAO extends Dynamo implements StoryDAOInterface {

    @Inject
    public DynamoStoryDAO() {}

    @Override
    public List<Status> getStory(String alias){
        // get list of 'alias' statuses

        return null;
    }

    // POST STATUS
    @Override
    public void postStatus(String alias, Status status){

        try {
            Table table = getDB().getTable("stories");
            System.out.println("Adding a new status...");

            // current timestamp in milliseconds
            long UNIXmillis = System.currentTimeMillis();

            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("sender_alias", alias, "timestamp", UNIXmillis)
                            .with("status", status));

            System.out.println("GetItem succeeded: " + outcome);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("[ServerError] - Unable to add %s's status", alias));
        }

    }

    List<Status> getDummyStatuses(){
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
