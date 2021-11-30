package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.FeedDAOInterface;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DynamoFeedDAO extends Dynamo implements FeedDAOInterface {

    @Inject
    public DynamoFeedDAO() {}

    @Override
    public void addFeed(String alias, Status status) {
        try {
            Table table = getDB().getTable("feeds");
            System.out.println("Adding a new item in feed...");

            // current timestamp in milliseconds
            long UNIXmillis = System.currentTimeMillis();

            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("receiver_alias", alias, "timestamp", UNIXmillis)
                            .with("status", status));

            System.out.println("GetItem succeeded: " + outcome);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to add to feed of " + alias);
        }
    }

    @Override
    public List<Status> getFeed(String alias, int limit, Status lastStatus) {
        // return list of statuses (alias' feed)

        Table table = getDB().getTable("feeds");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#a", "receiver_alias");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":aa", alias);

        QuerySpec querySpec;

        System.out.println("check if last status is null!!");

        if (lastStatus == null) { // get first 10 items
            querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                    .withNameMap(nameMap).withValueMap(valueMap)
                    .withMaxResultSize(limit);

        } else {
            // TODO: convert date of status to a unix timestamp
            String date = lastStatus.getDate();

            System.out.println("lastStatus date: " + date);

            int lastTimestamp = (int) Instant.parse(date).getEpochSecond();

            System.out.println("lastTimestamp: " + lastTimestamp);

            querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                    .withNameMap(nameMap).withValueMap(valueMap)
                    .withMaxResultSize(limit)
                    .withExclusiveStartKey("receiver_alias", alias, "timestamp", lastTimestamp);

        }

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Getting " + alias + "'s Feed");
            items = table.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getString("alias") + ": " + item.getString("post"));
            }

            return null;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get feed.");
        }
    }

    @Override
    public void updateFeed(String alias) {

        Table table = getDB().getTable("feeds");

//        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("receiver_alias", alias)
//                .withUpdateExpression("set " + updateAttribute + " = :ex")
//                .withValueMap(new ValueMap().withString(":ex", updatedValue))
//                .withReturnValues(ReturnValue.UPDATED_NEW);
//
//        try {
//            System.out.println("Updating the item...");
//            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//            System.out.println("UpdateItem succeeded:\n" + outcome);
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(String.format("[ServerError] - Unable to update %s's feed.", alias));
//
//        }
    }


}
