package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.dao.FeedDAOInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
                    .putItem(new Item().withPrimaryKey("follower_alias", alias, "timestamp", UNIXmillis)
                            .withJSON("status", JsonSerializer.serialize(status)));

            System.out.println("GetItem succeeded");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to add to feed " + e.getMessage());
        }
    }

    @Override
    public List<Status> getFeed(String alias, int limit, Status lastStatus, long lastTimestamp) {
        List<Status> statuses = new ArrayList<>();

        try {
            Table table = getDB().getTable("feeds");

            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#a", "follower_alias");
            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":aa", alias);

            QuerySpec querySpec;
            if (lastStatus == null) {
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withScanIndexForward(false)
                        .withMaxResultSize(limit);

            } else {
                querySpec = new QuerySpec().withKeyConditionExpression("#a = :aa")
                        .withNameMap(nameMap).withValueMap(valueMap)
                        .withScanIndexForward(false)
                        .withMaxResultSize(limit)
                        .withExclusiveStartKey("follower_alias", alias, "timestamp", lastTimestamp);
            }

            System.out.println("Getting " + alias + "'s feed");
            ItemCollection<QueryOutcome> items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();
                Status newStatus = JsonSerializer.deserialize(item.getJSON("status"), Status.class);
                statuses.add(newStatus);
                System.out.println("ITEM : " + item);
            }
            return statuses;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - unable to get feed " + e.getMessage());
        }
    }

    @Override
    public void feedBatchWrite(List<String> followers, String jsonStatus, long UNIXmillis) {
        // Constructor for TableWriteItems takes the name of the table
        TableWriteItems items = new TableWriteItems("feeds");

        // Add each user into the TableWriteItems object
        for (String follower : followers) {
            Item item = new Item()
                    .withPrimaryKey("follower_alias", follower, "timestamp", UNIXmillis)
                    .with("status", jsonStatus);
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items, "feed");
                items = new TableWriteItems("feeds");
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items, "feed");
        }
    }

}
