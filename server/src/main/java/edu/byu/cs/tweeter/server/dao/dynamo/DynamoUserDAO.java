package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.util.Pair;

public class DynamoUserDAO extends Dynamo implements UserDAOInterface {

    @Inject
    public DynamoUserDAO() {}

    @Override
    public void addUser(String alias, String firstName, String lastName, String url,
                        String hashedPassword, int followerCount, int followeeCount) {
        try {
            Table table = getDB().getTable("users");
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("alias", alias)
                            .withString("first_name", firstName)
                            .withString("last_name", lastName)
                            .withString("image_url", url)
                            .withString("password", hashedPassword)
                            .withNumber("follower_count", followerCount)
                            .withNumber("followee_count", followeeCount));

            System.out.println("GetItem succeeded: " + outcome);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to add user: " + alias);
        }
    }

    @Override
    public Pair<User,String> getUserByID(String alias) {
        Table table = getDB().getTable("users");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

        try {
            System.out.println("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome.toJSONPretty());

            String securePassword = (String) outcome.get("password");

            System.out.println("PRINTING PASSWORD: " + securePassword);

            String fname = (String) outcome.get("first_name");
            String lname = (String) outcome.get("last_name");
            String imageURL = (String) outcome.get("image_url");

            // firstName, lastName, imageUrl
            User user = new User(fname,lname,imageURL);
            user.setAlias(alias);

            System.out.println("RETREIVED USER: " + alias);

            return new Pair<>(user, securePassword);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get user: " + alias);
        }
    }

    @Override
    public void updateUser(String alias, String updateAttribute, String updatedValue) {

        Table table = getDB().getTable("users");

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", alias)
                .withUpdateExpression("set " + updateAttribute + " = :ex")
                .withValueMap(new ValueMap().withString(":ex", updatedValue))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to update user: " + alias);

        }
    }

    @Override
    public void deleteUser(String alias) {
        Table table = getDB().getTable("users");

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("alias", alias));
        try {
            System.out.println("Attempting a delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to delete user " + alias);
        }
    }
}
