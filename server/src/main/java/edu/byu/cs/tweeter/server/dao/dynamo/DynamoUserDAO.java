package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.sqs.DTO.UserDTO;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.util.Pair;

import java.util.List;

public class DynamoUserDAO extends Dynamo implements UserDAOInterface {

    @Inject
    public DynamoUserDAO() {}

    @Override
    public void addUser(String alias, String firstName, String lastName, String url,
                        String hashedPassword, int followerCount, int followeeCount) {
        try {
            Table table = getDB().getTable("users");
            System.out.println("Adding a new user...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("alias", alias)
                            .withString("first_name", firstName)
                            .withString("last_name", lastName)
                            .withString("image_url", url)
                            .withString("password", hashedPassword)
                            .withNumber("follower_count", followerCount)
                            .withNumber("followee_count", followeeCount));
            System.out.println("GetItem succeeded");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to add user " + e.getMessage());
        }
    }

    @Override
    public Pair<User,String> getUserByID(String alias) {
        try {
            Table table = getDB().getTable("users");

            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

            System.out.println("Attempting to get user...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome.toJSONPretty());

            String fname = (String) outcome.get("first_name");
            String lname = (String) outcome.get("last_name");
            String imageURL = (String) outcome.get("image_url");
            String securePassword = (String) outcome.get("password");

            User user = new User(fname,lname,imageURL);
            user.setAlias(alias);

            System.out.println("RETREIVED USER");

            return new Pair<>(user, securePassword);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get user " + e.getMessage());
        }
    }

    @Override
    public void updateUser(String alias, String updateAttribute, int updatedValue) {
        try {
            Table table = getDB().getTable("users");

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", alias)
                    .withUpdateExpression("set " + updateAttribute + " = :ex")
                    .withValueMap(new ValueMap().with(":ex", updatedValue))
                    .withReturnValues(ReturnValue.UPDATED_NEW);

            System.out.println("Updating user...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to update user " + e.getMessage());
        }
    }

    @Override
    public int getFollowingCount(String alias) {
        try {
            Table table = getDB().getTable("users");

            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

            System.out.println("Attempting to get following count...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded");

            return ((java.math.BigDecimal) outcome.get("followee_count")).intValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get following count " + e.getMessage());
        }
    }

    @Override
    public int getFollowerCount(String alias) {
        try {
            Table table = getDB().getTable("users");

            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

            System.out.println("Attempting to get follower count...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded");

            return ((java.math.BigDecimal) outcome.get("follower_count")).intValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get follower count " + e.getMessage());
        }
    }

}
