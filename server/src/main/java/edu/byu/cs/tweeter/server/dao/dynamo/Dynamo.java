package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.List;
import java.util.Map;

public class Dynamo {

    private static DynamoDB dynamoDB;

    public Dynamo() {

    }

    protected DynamoDB getDB(){
        if(dynamoDB == null){
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                    .withRegion("us-west-2")
                    .build();
            dynamoDB = new DynamoDB(client);
        }
        return dynamoDB;
    }

    protected void loopBatchWrite(TableWriteItems items, String itemType) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = getDB().batchWriteItem(items);
//        System.out.println(String.format("Wrote %s Batch", itemType));

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = getDB().batchWriteItemUnprocessed(unprocessedItems);
//            System.out.println(String.format("Wrote more %s", itemType));
        }
    }
}
