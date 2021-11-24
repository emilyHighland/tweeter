package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class Dynamo {

    protected DynamoDB dynamoDB;

    protected Dynamo() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();
        dynamoDB = new DynamoDB(client);
    }

    protected DynamoDB getDB(){
        return dynamoDB;
    }
}
