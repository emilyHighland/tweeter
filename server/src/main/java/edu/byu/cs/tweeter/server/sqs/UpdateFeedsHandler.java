package edu.byu.cs.tweeter.server.sqs;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.sqs.DTO.UpdateFeedDTO;
import edu.byu.cs.tweeter.server.handlers.config.InjectorConfig;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {

            System.out.println("Received msg from Queue2");

            // deserialize SQS event from UpdateFeedDTO
            UpdateFeedDTO deserializedMsg = JsonSerializer.deserialize(msg.getBody(), UpdateFeedDTO.class);

            // send followers batch and status to update their feeds
            Injector injector = InjectorConfig.getInstance().getInjector();
            StatusService statusService = injector.getInstance(StatusService.class);
            statusService.feedBatchWrite(deserializedMsg.getFollowers(), deserializedMsg.getStatus(), deserializedMsg.getUNIXmillis());

            System.out.println("Wrote batch to feed..");
        }
        return null;
    }
}
