package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAOInterface;

public class DynamoAuthTokenDAO extends Dynamo implements AuthTokenDAOInterface {

    @Inject
    public DynamoAuthTokenDAO() {}


    @Override
    public void addAuthToken(AuthToken authToken, String alias) {
        try {
            Table table = getDB().getTable("authTokens");

            // current timestamp in milliseconds
            long UNIXmillis = System.currentTimeMillis();

            System.out.println("Adding a new authToken...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("authToken", authToken.getToken())
                            .with("timestamp", UNIXmillis));
            System.out.println("Success adding authToken");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to add authToken" + e.getMessage());
        }
    }

    @Override
    public String getAuthToken(String authToken) {
        try {
            Table table = getDB().getTable("authTokens");

            GetItemSpec spec = new GetItemSpec().withPrimaryKey("authToken", authToken);

            System.out.println("Attempting to get authToken...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);

            return (String) outcome.get("authToken");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get authToken " + e.getMessage());
        }
    }

    @Override
    public void deleteAuthToken(String authToken) {
        try {
            Table table = getDB().getTable("authTokens");

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("authToken", authToken));

            System.out.println("Attempting to delete authToken...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to delete authToken " + e.getMessage());
        }
    }

}
