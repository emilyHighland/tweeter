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

    private String salt;

    @Inject
    public DynamoUserDAO() {}

    @Override
    public void addUser(String alias, String firstName, String lastName, String url,
                        String salt, String hashed,
                        int followerCount, int followeeCount) {
        try {
            Table table = getDB().getTable("users");
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("alias", alias)
                            .with("first_name", firstName)
                            .with("last_name", lastName)
                            .with("image_url", url)
                            .with("salt", salt)
                            .with("password", hashed)
                            .with("follower_count", followerCount)
                            .with("followee_count", followeeCount));

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
            System.out.println("GetItem succeeded: " + outcome);

            salt = (String) outcome.get("salt");
            String securePassword = (String) outcome.get("password");

//            User user = new User(outcome.);
//
//            return new Pair<>(user, securePassword);

            return null;

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
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

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

    @Override
    public String getUsersSalt(){
        return salt;
    }

}
