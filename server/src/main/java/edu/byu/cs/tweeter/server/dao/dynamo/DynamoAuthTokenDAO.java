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

        System.out.println(String.format("AUTHTOKEN = %s",authToken.getToken()));
        System.out.println("USER_ALIAS = " + alias);

        // current timestamp in milliseconds
        long UNIXmillis = System.currentTimeMillis();

        try {
            Table table = getDB().getTable("authTokens");

            System.out.println("Adding a new authToken...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("authToken", authToken.getToken())
                            .with("timestamp", UNIXmillis));

            System.out.println("Success adding authToken!!!\nOUTCOME = " + outcome);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to add authToken for " + alias);
        }
    }

    @Override
    public String getAuthToken(String alias) {

        Table table = getDB().getTable("authTokens");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

        try {
            System.out.println("Attempting to get authToken...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);

            return (String) outcome.get("authToken");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get authToken.");
        }
    }

    @Override
    public void deleteAuthToken(String authToken) {
        Table table = getDB().getTable("authTokens");

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("authToken", authToken));
        try {
            System.out.println("Attempting a delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to delete authToken.");
        }
    }

}
