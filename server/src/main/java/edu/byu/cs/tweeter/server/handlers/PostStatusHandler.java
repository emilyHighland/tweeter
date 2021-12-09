package edu.byu.cs.tweeter.server.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.sqs.DTO.PostStatusDTO;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest postStatusRequest, Context context) {

        long newTimestamp = System.currentTimeMillis();
        // Step 1. write a message to PostStatusQueue. (containing status posted and its owner's alias)
        // create DTO to send to SQS
        Status newStatus = postStatusRequest.getStatus();
        PostStatusDTO newPostDTO = new PostStatusDTO(postStatusRequest.getAlias(), newStatus, newTimestamp);

        System.out.println("Sending a single msg to Queue1..");

        // serialize DTO into a json string before sending message
        String messageBody = JsonSerializer.serialize(newPostDTO);
        String queueUrl = "https://sqs.us-west-2.amazonaws.com/024960643791/PostStatusQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
//                .withDelaySeconds(5);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);

        // Step 2. post status to user's story
        Injector injector = InjectorConfig.getInstance().getInjector();
        StatusService statusService = injector.getInstance(StatusService.class);
        return statusService.postStatus(postStatusRequest, newTimestamp);
    }
}
