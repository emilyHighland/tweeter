package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;

import java.time.Instant;
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



    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @return the followees.
     */
    @Override
    public List<User> getFollowees(String alias, int limit, String lastFollowee) {
        // return 'limit' number of followees of 'alias' from 'follows' table
        List<User> followees = new ArrayList<>();

        Table table = dynamoDB.getTable("follows");

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

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            System.out.println("Getting " + alias + "'s followees");
            items = table.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();

                String firstName = (String) item.get("followee_name");
                String newAlias = (String) item.get("followee_handle");

                System.out.println("firstNAme: " + firstName);
                System.out.println("newAlias: " + newAlias);

                // TODO: how do I get profile image of non-registered users?
                String newImageURL = "https://my-tweeter-bucket.s3.us-west-2.amazonaws.com/%40who_profile_image";
                User newUser = new User(firstName,"whoville", newAlias, newImageURL);
                followees.add(newUser);
                System.out.println("added new user");
            }

            return followees;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get followees.");
        }
    }

    @Override
    public List<User> getFollowers(String alias, int limit, String lastFollower) {
        // return 'limit' number of followers of 'alias' from 'follows' table
        List<User> followers = new ArrayList<>();

        Table table = dynamoDB.getTable("follows");


        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#a", "followee_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":aa", alias);

        QuerySpec querySpec;

        if (lastFollower == null) { // get first 10 items
            querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                    .withNameMap(nameMap).withValueMap(valueMap)
                    .withMaxResultSize(limit);
        } else {
            querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                    .withNameMap(nameMap).withValueMap(valueMap)
                    .withMaxResultSize(limit)
                    .withExclusiveStartKey("followee_handle", alias, "follower_handle", lastFollower);
        }

        System.out.println("query created!");

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Getting " + alias + "'s followees");
            items = table.getIndex("followee_handle-follower_handle-index").query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();

                String firstName = (String) item.get("follower_name");
                String newAlias = (String) item.get("follower_handle");

                System.out.println("firstNAme: " + firstName);
                System.out.println("newAlias: " + newAlias);

                // TODO: how do I get profile image of non-registered users?
                String newImageURL = "https://my-tweeter-bucket.s3.us-west-2.amazonaws.com/%40who_profile_image";
                User newUser = new User(firstName,"whoville", newAlias, newImageURL);
                followers.add(newUser);
            }

            return followers;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get followees.");
        }
    }

    @Override
    public void follow(String alias){
        assert alias != null;

        // TODO: increment followee count
        // update() count:
        //  ++ currentUser's following count
        //  ++ the followee's followers count

    }

    @Override
    public void unfollow(String alias){
        assert alias != null;

        // TODO: decrement followee count
        // update() count:
        //  -- currentUser's following count
        //  -- the followee's followers count

    }

}
