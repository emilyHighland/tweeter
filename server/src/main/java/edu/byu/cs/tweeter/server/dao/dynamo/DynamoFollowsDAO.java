package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowsDAO extends Dynamo implements FollowsDAOInterface {

    @Inject
    public DynamoFollowsDAO() {}


    @Override
    public List<User> getFollowees(String alias, int limit, String lastFollowee) {
        List<User> followees = new ArrayList<>();

        try {
            Table table = getDB().getTable("follows");

            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#a", "follower_handle");
            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":aa", alias);

            QuerySpec querySpec;
            if (lastFollowee == null) { // get first 10 items
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withMaxResultSize(limit);
            } else {
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withMaxResultSize(limit)
                        .withExclusiveStartKey("follower_handle", alias, "followee_handle", lastFollowee);
            }

            System.out.println("Getting " + alias + "'s following");

            ItemCollection<QueryOutcome> items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();

                if (item == null){
                    break;
                }

                String name = (String) item.get("followee_name");
                String[] names = name.split(" ");
                String newAlias = (String) item.get("followee_handle");
                String newImage = (String) item.get("followee_image");

                User newUser = new User(names[0],names[1], newAlias, newImage);
                followees.add(newUser);

                System.out.println("+ ITEM");
            }
            return followees;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get following " + e.getMessage());
        }
    }

    @Override
    public List<User> getFollowers(String alias, int limit, String lastFollower) {
        List<User> followers = new ArrayList<>();

        try {
            Table table = getDB().getTable("follows");

            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#a", "followee_handle");
            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":aa", alias);

            QuerySpec querySpec;
            if (lastFollower == null) {
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withMaxResultSize(limit);
            } else {
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withMaxResultSize(limit)
                        .withExclusiveStartKey("followee_handle", alias, "follower_handle", lastFollower);
            }

            System.out.println("Getting " + alias + "'s followers");

            ItemCollection<QueryOutcome> items = table.getIndex("followee_handle-follower_handle-index").query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();

                if (item == null){
                    break;
                }

                String name = (String) item.get("name");
                String[] names = name.split(" ");
                String newImage = "https://my-tweeter-bucket.s3.us-west-2.amazonaws.com/%40rup_profile_image";

                String newAlias = (String) item.get("follower_handle");

                User newUser = new User(names[0],names[1], newAlias, newImage);
                followers.add(newUser);

                System.out.println("+ ITEM");
            }
            return followers;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get followers " + e.getMessage());
        }
    }

    @Override
    public void add(String followeeAlias, String currAlias, String followeeName, String currName, String followeeImage, String currUserImage){
        try {
            Table table = getDB().getTable("follows");

            System.out.println("Adding a new follows item...");

            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("follower_handle", currAlias,
                            "followee_handle", followeeAlias)
                            .with("follower_name", currName)
                            .with("followee_name", followeeName)
                            .with("followee_image", followeeImage)
                            .with("follower_image", currUserImage));
            System.out.println("GetItem succeeded");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to add follows item " + e.getMessage());
        }
    }

    @Override
    public void delete(String currAlias, String followeeAlias){
        try {
            Table table = getDB().getTable("follows");

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey(new PrimaryKey("follower_handle", currAlias,
                            "followee_handle", followeeAlias));

            System.out.println("Attempting to delete follows item...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to delete follows item " + e.getMessage());
        }
    }

    @Override
    public boolean find(String followerAlias, String followeeAlias) {
        try {
            Table table = getDB().getTable("follows");

            GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey("follower_handle", followerAlias,
                            "followee_handle", followeeAlias);

            System.out.println("Attempting to find a follows item...");
            Item outcome = table.getItem(getItemSpec);
            System.out.println("GetItem succeeded: " + outcome);
            return outcome != null;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to find follows item " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllFollowerAliases(String followeeAlias) {
        int page_size = 25;
        List<String> followerAliases = new ArrayList<>();

        try {
            Table table = getDB().getTable("follows");

            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#a", "followee_handle");
            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":aa", followeeAlias);
            QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap).withMaxResultSize(1000);

            System.out.println("Getting " + followeeAlias + "'s follower aliases");

            ItemCollection<QueryOutcome> items = table.getIndex("followee_handle-follower_handle-index").query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();
                String newAlias = (String) item.get("follower_handle");
                followerAliases.add(newAlias);
            }
            return followerAliases;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get follower aliases " + e.getMessage());
        }
    }

    @Override
    public void addFollowersBatch(List<String> followers, String followeeTarget) {
        // Constructor for TableWriteItems takes the name of the table
        TableWriteItems items = new TableWriteItems("follows");

        // Add each user into the TableWriteItems object
        for (String follower : followers) {
            Item item = new Item()
                    .withPrimaryKey("follower_handle", follower,
                            "followee_handle", followeeTarget);
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items, "followers");
                items = new TableWriteItems("follows");
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items, "followers");
        }
    }
}
