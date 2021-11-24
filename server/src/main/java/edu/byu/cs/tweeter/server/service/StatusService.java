package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAOInterface;
import edu.byu.cs.tweeter.server.dao.StoryDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.util.FakeData;

import java.util.ArrayList;
import java.util.List;

public class StatusService {

    private final DAOFactoryInterface factory;

    @Inject
    public StatusService(DAOFactoryInterface factory){
        this.factory = factory;
    }


    /** STORY */
    public StoryResponse getStory(StoryRequest request){
        try {
            StoryDAOInterface sdao = this.factory.getStoryDAO();


            // TODO: Generates dummy data. Replace with real implementation.
            assert request.getLimit() > 0;
            assert request.getTargetAlias() != null;

            List<Status> allStatuses = sdao.getStory(request.getTargetAlias()); //getDummyStatuses();
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


        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
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


    /** FEED */
    public FeedResponse getFeed(FeedRequest request){
        try {

            // TODO: Generates dummy data. Replace with real implementation.
            assert request.getLimit() > 0;
            assert request.getTargetAlias() != null;

            FeedDAOInterface fdao = this.factory.getFeedDAO();
            List<Status> allStatuses = fdao.getFeed(request.getTargetAlias());
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

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
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


    /** POST STATUS */
    public SimpleResponse postStatus(PostStatusRequest request){
        try {
            assert request.getPost() != null;
            assert request.getAlias() != null;
            StoryDAOInterface sdao = this.factory.getStoryDAO();
            sdao.postStatus(request.getAlias(), request.getPost());

            return new SimpleResponse();

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }





    List<Status> getDummyStatuses(){
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
