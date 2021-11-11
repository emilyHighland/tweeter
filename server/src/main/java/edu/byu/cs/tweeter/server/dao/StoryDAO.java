package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.util.FakeData;

import java.util.ArrayList;
import java.util.List;

public class StoryDAO {

    public StoryResponse getStory(StoryRequest request){
        // TODO: Generates dummy data. Replace with real implementation.
        assert request.getLimit() > 0;
        assert request.getTargetAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

                for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++,limitCounter++){
                    responseStory.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }
        return new StoryResponse(responseStory, hasMorePages);
    }

    private int getStoryStartingIndex(Status lastStatus, List<Status> allStatuses){
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

    // POST STATUS
    public PostStatusResponse postStatus(PostStatusRequest request){
        assert request.getPost() != null;
        assert request.getAlias() != null;

        return new PostStatusResponse();
    }

    List<Status> getDummyStatuses(){
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
