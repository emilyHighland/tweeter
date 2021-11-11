package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.util.FakeData;

import java.util.ArrayList;
import java.util.List;

public class FeedDAO {

    public FeedResponse getFeed(FeedRequest request){
        // TODO: Generates dummy data. Replace with real implementation.
        assert request.getLimit() > 0;
        assert request.getTargetAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

                for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++,limitCounter++){
                    responseFeed.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }
        return new FeedResponse(responseFeed, hasMorePages);
    }

    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatuses){
        int statusIndex = 0;

        if (lastStatus != null){
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }

    List<Status> getDummyStatuses(){
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
