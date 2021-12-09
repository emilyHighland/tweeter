package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.dao.StoryDAOInterface;

import java.util.*;

public class DynamoStoryDAO extends Dynamo implements StoryDAOInterface {

    @Inject
    public DynamoStoryDAO() {}

    // POST STATUS
    @Override
    public void addStatus(String alias, Status status, long UNIXmillis){
        try {
            Table table = getDB().getTable("stories");
            System.out.println("Adding a new item to story...");

            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("sender_alias", alias, "timestamp", UNIXmillis)
                            .withJSON("status", JsonSerializer.serialize(status)));

            System.out.println("GetItem succeeded");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to add to story " + e.getMessage());
        }
    }

    @Override
    public List<Status> getStory(String alias, int limit, Status lastStatus, long lastTimestamp){
        List<Status> statuses = new ArrayList<>();

        try {
            Table table = getDB().getTable("stories");

            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#a", "sender_alias");
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
                        .withExclusiveStartKey("sender_alias", alias, "timestamp", lastTimestamp);
            }

            System.out.println("Getting " + alias + "'s Story");

            ItemCollection<QueryOutcome> items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            while (iterator.hasNext()) {
                item = iterator.next();
                Status newStatus = JsonSerializer.deserialize(item.getJSON("status"), Status.class);
                statuses.add(newStatus);

                System.out.println("ITEM = " + item);
            }
//            Map<String, AttributeValue> lastKey = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
//            System.out.println("GOT LAST KEY");
//
//            if (lastKey == null){
//                return null;
//            } else {
//                int previousTimestamp = ((java.math.BigDecimal) item.get("timestamp")).intValue();
            return statuses;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[ServerError] - Unable to get story " + e.getMessage());
        }
    }
}
