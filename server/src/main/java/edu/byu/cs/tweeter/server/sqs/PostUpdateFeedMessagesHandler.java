package edu.byu.cs.tweeter.server.sqs;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.sqs.DTO.PostStatusDTO;
import edu.byu.cs.tweeter.server.sqs.DTO.UpdateFeedDTO;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.FollowService;

import java.util.List;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("Received msg from Queue1..");

            // deserialize PostStatusQueue message
            PostStatusDTO deserializedMsg = JsonSerializer.deserialize(msg.getBody(),PostStatusDTO.class);

            // get batch of followers (1000)
            Injector injector = InjectorConfig.getInstance().getInjector();
            FollowService followService = injector.getInstance(FollowService.class);
            List<String> allFollowers = followService.getFollowersBatches(deserializedMsg.getUserAlias());


            // for all followers: break up into batches to send to queue2
            for (int i = 0; i < allFollowers.size(); i+=25) {

                // create DTO to send to Queue2
                UpdateFeedDTO feedDTO = new UpdateFeedDTO(deserializedMsg.getUserAlias(), allFollowers.subList(i, i+24), deserializedMsg.getNewStatus(),
                        deserializedMsg.getUNIXmillis());

                // serialize DTO into a json string before sending message
                String messageBody = JsonSerializer.serialize(feedDTO);
                String queueUrl = "https://sqs.us-west-2.amazonaws.com/024960643791/UpdateFeedQueue";

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(messageBody);
//                    .withDelaySeconds(5);

                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

                System.out.println("Sending msg to Queue2");

                String msgId = send_msg_result.getMessageId();
                System.out.println("Message ID: " + msgId);

            }

            System.out.println("sent allllll the follower & status batches!!!");
        }
        return null;
    }
}
